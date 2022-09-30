package com.pickpick.support;

import com.pickpick.channel.domain.Channel;
import com.pickpick.config.SlackProperties;
import com.pickpick.exception.SlackApiCallException;
import com.pickpick.exception.message.SlackSendMessageFailureException;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Reminder;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.oauth.OAuthV2AccessRequest;
import com.slack.api.methods.request.users.UsersIdentityRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.User;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SlackClient {

    private static final String REMINDER_TEXT_FORMAT =
            "============================ \n " +
                    "*리마인드 메시지가 도착했습니다*!\uD83D\uDC39 \n" +
                    "============================ \n %s";

    private final SlackProperties slackProperties;
    private final MethodsClient methodsClient;

    public SlackClient(final SlackProperties slackProperties, final MethodsClient methodsClient) {
        this.slackProperties = slackProperties;
        this.methodsClient = methodsClient;
    }

    public String findAccessToken(final String code) {
        OAuthV2AccessRequest request = OAuthV2AccessRequest.builder()
                .clientId(slackProperties.getClientId())
                .clientSecret(slackProperties.getClientSecret())
                .redirectUri(slackProperties.getRedirectUrl())
                .code(code)
                .build();

        try {
            return methodsClient.oauthV2Access(request)
                    .getAuthedUser()
                    .getAccessToken();
        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException("oauthV2Access");
        }
    }

    public String findMemberSlackId(final String accessToken) {
        UsersIdentityRequest request = UsersIdentityRequest.builder()
                .token(accessToken)
                .build();

        try {
            return methodsClient.usersIdentity(request)
                    .getUser()
                    .getId();
        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException("usersIdentity");
        }
    }

    public Channel findChannel(final String channelSlackId) {
        try {
            Conversation conversation = methodsClient
                    .conversationsInfo(request -> request.channel(channelSlackId))
                    .getChannel();

            return new Channel(conversation.getId(), conversation.getName());

        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException("conversationsInfo");
        }
    }

    public List<Member> findAllWorkspaceMembers() {
        try {
            List<User> users = methodsClient.usersList(request -> request)
                    .getMembers();
            return toMembers(users);
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
        return new Member(user.getId(), user.getProfile().getDisplayName(), user.getProfile().getImage48());
    }

    public void sendMessage(final Reminder reminder)
            throws IOException, SlackApiException, SlackSendMessageFailureException {

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(reminder.getMember().getSlackId())
                .text(String.format(REMINDER_TEXT_FORMAT, reminder.getMessage().getText()))
                .build();

        ChatPostMessageResponse response = methodsClient.chatPostMessage(request);

        if (!response.isOk()) {
            throw new SlackSendMessageFailureException(response.getError());
        }
    }
}
