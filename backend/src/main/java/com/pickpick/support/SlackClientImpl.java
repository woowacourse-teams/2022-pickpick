package com.pickpick.support;

import com.pickpick.channel.domain.Channel;
import com.pickpick.config.SlackProperties;
import com.pickpick.exception.SlackApiCallException;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Reminder;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.SlackApiTextResponse;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.oauth.OAuthV2AccessRequest;
import com.slack.api.methods.request.users.UsersIdentityRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsInfoResponse;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse;
import com.slack.api.methods.response.users.UsersIdentityResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.User;
import com.slack.api.model.User.Profile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SlackClientImpl implements SlackClient {

    private static final String REMINDER_TEXT_FORMAT =
            "============================ \n " +
                    "*리마인드 메시지가 도착했습니다*!\uD83D\uDC39 \n" +
                    "============================ \n %s";

    private final SlackProperties slackProperties;
    private final MethodsClient methodsClient;

    public SlackClientImpl(final SlackProperties slackProperties, final MethodsClient methodsClient) {
        this.slackProperties = slackProperties;
        this.methodsClient = methodsClient;
    }

    public String callAccessToken(final String code) {
        OAuthV2AccessRequest request = generateOAuthRequest(code);

        try {
            OAuthV2AccessResponse response = methodsClient.oauthV2Access(request);
            validateResponse("oauthV2Access", response);
            return response.getAuthedUser().getAccessToken();

        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException("oauthV2Access");
        }
    }

    private OAuthV2AccessRequest generateOAuthRequest(final String code) {
        return OAuthV2AccessRequest.builder()
                .clientId(slackProperties.getClientId())
                .clientSecret(slackProperties.getClientSecret())
                .redirectUri(slackProperties.getRedirectUrl())
                .code(code)
                .build();
    }

    public String callMemberSlackId(final String accessToken) {
        UsersIdentityRequest request = UsersIdentityRequest.builder()
                .token(accessToken)
                .build();

        try {
            UsersIdentityResponse response = methodsClient.usersIdentity(request);
            validateResponse("usersIdentity", response);
            return response.getUser().getId();
        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException("usersIdentity");
        }
    }

    public Channel callChannel(final String channelSlackId) {
        try {
            ConversationsInfoResponse response = methodsClient.conversationsInfo(
                    request -> request.channel(channelSlackId));
            validateResponse("conversationsInfo", response);

            Conversation conversation = response.getChannel();
            return new Channel(conversation.getId(), conversation.getName());

        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException("conversationsInfo");
        }
    }

    public List<Member> findAllWorkspaceMembers() {
        try {
            UsersListResponse response = methodsClient.usersList(request -> request);
            validateResponse("usersList", response);
            return toMembers(response.getMembers());
        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException("usersList");
        }
    }

    private List<Member> toMembers(final List<User> users) {
        return users.stream()
                .map(this::toMember)
                .collect(Collectors.toList());
    }

    private Member toMember(final User user) {
        Profile profile = user.getProfile();
        String username = profile.getDisplayName();
        if (username.isBlank()) {
            username = profile.getRealName();
        }
        return new Member(user.getId(), username, profile.getImage48());
    }

    public void sendMessage(final Reminder reminder) {
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(reminder.getMember().getSlackId())
                .text(String.format(REMINDER_TEXT_FORMAT, reminder.getMessage().getText()))
                .build();

        try {
            ChatPostMessageResponse response = methodsClient.chatPostMessage(request);
            validateResponse("chatPostMessage", response);
        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException(e);
        }
    }

    private <T extends SlackApiTextResponse> void validateResponse(final String methodName, final T response) {
        if (!response.isOk()) {
            throw new SlackApiCallException(methodName, response.getError());
        }
    }
}
