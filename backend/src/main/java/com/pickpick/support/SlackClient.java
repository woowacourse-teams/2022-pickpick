package com.pickpick.support;

import com.pickpick.auth.application.dto.BotInfoDto;
import com.pickpick.channel.domain.Channel;
import com.pickpick.config.SlackProperties;
import com.pickpick.exception.SlackApiCallException;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Reminder;
import com.pickpick.workspace.domain.Workspace;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.SlackApiTextResponse;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.conversations.ConversationsInviteRequest;
import com.slack.api.methods.request.oauth.OAuthV2AccessRequest;
import com.slack.api.methods.request.users.UsersIdentityRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsInfoResponse;
import com.slack.api.methods.response.conversations.ConversationsInviteResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
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
public class SlackClient implements ExternalClient {

    private static final String OAUTH_ACCESS_METHOD_NAME = "oauthV2Access";
    private static final String USERS_IDENTITY_METHOD_NAME = "usersIdentity";
    private static final String USER_LIST_METHOD_NAME = "usersList";
    private static final String CHANNEL_INFO_METHOD_NAME = "conversationsInfo";
    private static final String CHANNEL_LIST_METHOD_NAME = "conversationsList";
    private static final String CHANNEL_INVITE_METHOD_NAME = "conversationsInvite";
    private static final String CHAT_POST_METHOD_NAME = "chatPostMessage";
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

    @Override
    public String callUserToken(final String code) {
        OAuthV2AccessRequest request = generateOAuthRequest(code);

        try {
            OAuthV2AccessResponse response = methodsClient
                    .oauthV2Access(request);
            validateResponse(OAUTH_ACCESS_METHOD_NAME, response);
            return response.getAuthedUser().getAccessToken();

        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException(OAUTH_ACCESS_METHOD_NAME);
        }
    }

    @Override
    public BotInfoDto callBotInfo(final String code) {
        OAuthV2AccessRequest request = generateOAuthRequest(code);

        try {
            OAuthV2AccessResponse response = methodsClient
                    .oauthV2Access(request);
            validateResponse(OAUTH_ACCESS_METHOD_NAME, response);

            return new BotInfoDto(response.getTeam().getId(), response.getAccessToken());

        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException(OAUTH_ACCESS_METHOD_NAME);
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

    @Override
    public String callMemberSlackId(final String accessToken) {
        UsersIdentityRequest request = UsersIdentityRequest.builder()
                .token(accessToken)
                .build();

        try {
            UsersIdentityResponse response = methodsClient.usersIdentity(request);
            validateResponse(USERS_IDENTITY_METHOD_NAME, response);
            return response.getUser().getId();
        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException(USERS_IDENTITY_METHOD_NAME);
        }
    }

    @Override
    public Channel callChannel(final String channelSlackId, final Workspace workspace) {
        try {
            ConversationsInfoResponse response = methodsClient.conversationsInfo(
                    request -> request.channel(channelSlackId)
                            .token(workspace.getBotToken())
            );
            validateResponse(CHANNEL_INFO_METHOD_NAME, response);

            Conversation conversation = response.getChannel();
            return new Channel(conversation.getId(), conversation.getName(), workspace);

        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException(CHANNEL_INFO_METHOD_NAME);
        }
    }

    @Override
    public List<Member> findAllWorkspaceMembers(final Workspace workspace) {
        try {
            UsersListResponse response = methodsClient.usersList(request -> request.token(workspace.getBotToken()));
            validateResponse(USER_LIST_METHOD_NAME, response);
            return toMembers(response.getMembers(), workspace);
        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException(USER_LIST_METHOD_NAME);
        }
    }

    private List<Member> toMembers(final List<User> users, final Workspace workspace) {
        return users.stream()
                .map(user -> toMember(user, workspace))
                .collect(Collectors.toList());
    }

    private Member toMember(final User user, final Workspace workspace) {
        Profile profile = user.getProfile();
        String username = profile.getDisplayName();
        if (username.isBlank()) {
            username = profile.getRealName();
        }
        return new Member(user.getId(), username, profile.getImage48(), workspace);
    }

    @Override
    public List<Channel> findAllWorkspaceChannels(final Workspace workspace) {
        try {
            ConversationsListResponse response = methodsClient.conversationsList(
                    request -> request.token(workspace.getBotToken()));
            validateResponse(CHANNEL_LIST_METHOD_NAME, response);
            return toChannels(response.getChannels(), workspace);
        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException(CHANNEL_LIST_METHOD_NAME);
        }
    }

    private List<Channel> toChannels(final List<Conversation> channels, final Workspace workspace) {
        return channels.stream()
                .map(channel -> toChannel(channel, workspace))
                .collect(Collectors.toList());
    }

    private Channel toChannel(final Conversation channel, final Workspace workspace) {
        return new Channel(channel.getId(), channel.getName(), workspace);
    }

    @Override
    public void sendMessage(final Reminder reminder) {
        Member member = reminder.getMember();
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(member.getSlackId())
                .text(String.format(REMINDER_TEXT_FORMAT, reminder.getMessage().getText()))
                .token(member.getWorkspace().getBotToken())
                .build();

        try {
            ChatPostMessageResponse response = methodsClient.chatPostMessage(request);
            validateResponse(CHAT_POST_METHOD_NAME, response);
        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException(CHAT_POST_METHOD_NAME);
        }
    }

    @Override
    public void inviteBotToChannel(final Member member, final Channel channel) {
        ConversationsInviteRequest request = ConversationsInviteRequest.builder()
                .channel(channel.getSlackId())
                .token(member.getToken())
                .users(List.of(member.getWorkspace().getBotToken()))
                .build();

        try {
            ConversationsInviteResponse response = methodsClient.conversationsInvite(request);
            if (isBotAlreadyInChannel(response)) {
                return;
            }
            validateResponse(CHANNEL_INVITE_METHOD_NAME, response);
        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException(CHANNEL_INVITE_METHOD_NAME);
        }
    }

    private boolean isBotAlreadyInChannel(final ConversationsInviteResponse response) {
        return "already_in_channel".equals(response.getError());
    }

    private <T extends SlackApiTextResponse> void validateResponse(final String methodName, final T response) {
        if (!response.isOk()) {
            throw new SlackApiCallException(methodName, response.getError());
        }
    }
}
