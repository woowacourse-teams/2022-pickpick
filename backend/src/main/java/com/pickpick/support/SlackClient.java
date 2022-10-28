package com.pickpick.support;

import com.pickpick.auth.application.dto.MemberInfoDto;
import com.pickpick.auth.application.dto.OAuthAccessInfoDto;
import com.pickpick.channel.domain.Channel;
import com.pickpick.config.SlackProperties;
import com.pickpick.exception.SlackApiCallException;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Reminder;
import com.pickpick.slackevent.domain.Participation;
import com.pickpick.workspace.domain.Workspace;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.SlackApiRequest;
import com.slack.api.methods.SlackApiTextResponse;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.conversations.ConversationsInviteRequest;
import com.slack.api.methods.request.conversations.ConversationsListRequest;
import com.slack.api.methods.request.oauth.OAuthV2AccessRequest;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.response.conversations.ConversationsInviteResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse.AuthedUser;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.User;
import com.slack.api.model.User.Profile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SlackClient implements ExternalClient {

    private static final String LOGGING_INFO = "[Request] Slack Api Request - {}";
    private static final String OAUTH_ACCESS_METHOD_NAME = "oauthV2Access";
    private static final String USERS_IDENTITY_METHOD_NAME = "usersIdentity";
    private static final String USER_LIST_METHOD_NAME = "usersList";
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
    public OAuthAccessInfoDto callOAuthAccessInfo(final String code) {
        OAuthV2AccessResponse response = callOAuth2(code, slackProperties.getWorkspaceRedirectUrl());
        AuthedUser user = response.getAuthedUser();

        return new OAuthAccessInfoDto(response.getTeam().getId(), response.getAccessToken(), response.getBotUserId(),
                user.getAccessToken(), user.getId());
    }

    @Override
    public MemberInfoDto callMemberInfo(final String code) {
        OAuthV2AccessResponse response = callOAuth2(code, slackProperties.getLoginRedirectUrl());

        return new MemberInfoDto(response.getAuthedUser().getId(), response.getAuthedUser().getAccessToken());
    }

    private OAuthV2AccessResponse callOAuth2(final String code, final String redirectUrl) {
        OAuthV2AccessRequest request = generateOAuthRequest(code, redirectUrl);
        return execute(methodsClient::oauthV2Access, OAUTH_ACCESS_METHOD_NAME, request);
    }

    private OAuthV2AccessRequest generateOAuthRequest(final String code, final String redirectUrl) {
        return OAuthV2AccessRequest.builder()
                .clientId(slackProperties.getClientId())
                .clientSecret(slackProperties.getClientSecret())
                .redirectUri(redirectUrl)
                .code(code)
                .build();
    }

    @Override
    public List<Member> findMembersByWorkspace(final Workspace workspace) {
        UsersListRequest request = UsersListRequest.builder()
                .token(workspace.getBotToken())
                .build();

        UsersListResponse response = execute(
                methodsClient::usersList,
                USER_LIST_METHOD_NAME,
                request);

        return toMembers(response.getMembers(), workspace);
    }

    private List<Member> toMembers(final List<User> users, final Workspace workspace) {
        return users.stream()
                .map(user -> toMember(user, workspace))
                .filter(this::isNotSlackBot)
                .collect(Collectors.toList());
    }

    private boolean isNotSlackBot(final Member member) {
        return !"USLACKBOT".equalsIgnoreCase(member.getSlackId());
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
    public List<Channel> findChannelsByWorkspace(final Workspace workspace) {
        ConversationsListResponse response = findChannels(workspace.getBotToken());
        return toChannels(response.getChannels(), workspace);
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
    public Participation findChannelParticipation(final String userToken) {
        ConversationsListResponse response = findChannels(userToken);

        Map<String, Boolean> participation = response.getChannels()
                .stream()
                .collect(Collectors.toMap(Conversation::getId, Conversation::isMember));

        return new Participation(participation);
    }

    private ConversationsListResponse findChannels(String accessToken) {
        ConversationsListRequest request = ConversationsListRequest.builder()
                .token(accessToken)
                .build();

        return execute(
                methodsClient::conversationsList,
                CHANNEL_LIST_METHOD_NAME,
                request);
    }

    @Override
    public void sendMessage(final Reminder reminder) {
        Member member = reminder.getMember();
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(member.getSlackId())
                .text(String.format(REMINDER_TEXT_FORMAT, reminder.getMessage().getText()))
                .token(member.getWorkspace().getBotToken())
                .build();

        execute(methodsClient::chatPostMessage, CHAT_POST_METHOD_NAME, request);
    }

    @Override
    public void inviteBotToChannel(final Member member, final Channel channel) {
        ConversationsInviteRequest request = ConversationsInviteRequest.builder()
                .channel(channel.getSlackId())
                .token(member.getSlackToken())
                .users(List.of(member.getWorkspace().getBotSlackId()))
                .build();
        log.info(LOGGING_INFO, request);

        try {
            ConversationsInviteResponse response = methodsClient.conversationsInvite(request);
            if (isBotAlreadyInChannel(response)) {
                return;
            }
            validateResponse(CHANNEL_INVITE_METHOD_NAME, response);
        } catch (IOException | SlackApiException e) {
            log.error(CHANNEL_INVITE_METHOD_NAME, e);
            throw new SlackApiCallException(CHANNEL_INVITE_METHOD_NAME);
        }
    }

    private <T extends SlackApiTextResponse, K extends SlackApiRequest> T execute(
            final SlackFunction<T, K> slackFunction, final String methodName, final K request) {
        log.info(LOGGING_INFO, request);

        try {
            T result = slackFunction.execute(request);
            validateResponse(methodName, result);
            return result;
        } catch (IOException | SlackApiException e) {
            log.error(methodName, e);
            throw new SlackApiCallException(methodName);
        }
    }

    private boolean isBotAlreadyInChannel(final ConversationsInviteResponse response) {
        return !response.isOk() && "already_in_channel".equals(response.getError());
    }

    private <T extends SlackApiTextResponse> void validateResponse(final String methodName, final T response) {
        if (!response.isOk()) {
            throw new SlackApiCallException(methodName, response);
        }
    }
}
