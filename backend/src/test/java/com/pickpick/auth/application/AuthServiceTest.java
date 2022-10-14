package com.pickpick.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.auth.ui.dto.LoginResponse;
import com.pickpick.exception.auth.ExpiredTokenException;
import com.pickpick.exception.auth.InvalidTokenException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import com.slack.api.RequestConfigurator;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsListRequest.ConversationsListRequestBuilder;
import com.slack.api.methods.request.oauth.OAuthV2AccessRequest;
import com.slack.api.methods.request.users.UsersIdentityRequest;
import com.slack.api.methods.request.users.UsersListRequest.UsersListRequestBuilder;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse.AuthedUser;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse.Team;
import com.slack.api.methods.response.users.UsersIdentityResponse;
import com.slack.api.methods.response.users.UsersIdentityResponse.User;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.User.Profile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AuthServiceTest {

    @MockBean
    private MethodsClient slackClient;

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository members;

    @Autowired
    private WorkspaceRepository workspaces;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("로그인 시 토큰이 발급된다.")
    @Test
    void login() throws SlackApiException, IOException {
        // given
        Workspace workspace = workspaces.save(new Workspace("slackId", "botToken", "botSlackId"));
        Member member = members.save(new Member("slackId", "username", "thumbnail.png"));

        given(slackClient.oauthV2Access(any(OAuthV2AccessRequest.class)))
                .willReturn(generateOAuthV2AccessResponse(workspace.getSlackId()));
        given(slackClient.usersIdentity(any(UsersIdentityRequest.class)))
                .willReturn(generateUsersIdentityResponse(member.getSlackId()));

        // when
        LoginResponse response = authService.login("1234");

        // then
        assertThat(response.getToken()).isNotEmpty();
    }

    @DisplayName("최초 로그인 시 isFirstLogin true, 두번째 이후부턴 false가 반환되어야 한다")
    @Test
    void firstLogin() throws SlackApiException, IOException {
        // given
        Workspace workspace = workspaces.save(new Workspace("slackId", "botToken", "botSlackId"));
        Member member = members.save(new Member("slackId", "username", "thumbnail.png"));

        given(slackClient.oauthV2Access(any(OAuthV2AccessRequest.class)))
                .willReturn(generateOAuthV2AccessResponse(workspace.getSlackId()));
        given(slackClient.usersIdentity(any(UsersIdentityRequest.class)))
                .willReturn(generateUsersIdentityResponse(member.getSlackId()));

        // when
        LoginResponse firstLoginResponse = authService.login("1234");
        LoginResponse secondLoginResponse = authService.login("1234");

        // then
        assertAll(
                () -> assertThat(firstLoginResponse.getToken()).isNotEmpty(),
                () -> assertThat(firstLoginResponse.isFirstLogin()).isTrue(),
                () -> assertThat(secondLoginResponse.getToken()).isNotEmpty(),
                () -> assertThat(secondLoginResponse.isFirstLogin()).isFalse()
        );
    }

    @DisplayName("워크스페이스 초기화 후 로그인한다")
    @Test
    void registerWorkspace() throws SlackApiException, IOException {
        // given
        String memberSlackId = "U123456";
        String workspaceSlackId = "T123456";

        given(slackClient.oauthV2Access(any(OAuthV2AccessRequest.class)))
                .willReturn(generateOAuthV2AccessResponse(workspaceSlackId));
        given(slackClient.conversationsList((RequestConfigurator<ConversationsListRequestBuilder>) any()))
                .willReturn(generateConversationsListResponse());
        given(slackClient.usersList((RequestConfigurator<UsersListRequestBuilder>) any()))
                .willReturn(generateUsersListResponse(memberSlackId));
        given(slackClient.usersIdentity(any(UsersIdentityRequest.class)))
                .willReturn(generateUsersIdentityResponse(memberSlackId));

        // when
        LoginResponse response = authService.registerWorkspace("code");

        // then
        assertThat(response.getToken()).isNotEmpty();
        assertThat(workspaces.findBySlackId(workspaceSlackId)).isNotEmpty();
    }

    private OAuthV2AccessResponse generateOAuthV2AccessResponse(final String workspaceSlackId) {
        OAuthV2AccessResponse response = new OAuthV2AccessResponse();

        AuthedUser authedUser = new AuthedUser();
        authedUser.setAccessToken("token");

        Team team = new Team();
        team.setId(workspaceSlackId);

        response.setOk(true);
        response.setAuthedUser(authedUser);
        response.setTeam(team);
        response.setAccessToken("botToken");
        response.setBotUserId("botSlackId");

        return response;
    }

    private UsersListResponse generateUsersListResponse(final String memberSlackId) {
        UsersListResponse response = new UsersListResponse();

        com.slack.api.model.User member = new com.slack.api.model.User();
        Profile profile = new Profile();
        profile.setDisplayName("연로그");
        profile.setImage48("image48.png");

        member.setId(memberSlackId);
        member.setProfile(profile);

        response.setOk(true);
        response.setMembers(List.of(member));

        return response;
    }

    private UsersIdentityResponse generateUsersIdentityResponse(final String memberSlackId) {
        UsersIdentityResponse usersIdentityResponse = new UsersIdentityResponse();

        User user = new User();
        user.setId(memberSlackId);

        usersIdentityResponse.setOk(true);
        usersIdentityResponse.setUser(user);

        return usersIdentityResponse;
    }

    private ConversationsListResponse generateConversationsListResponse() {
        ConversationsListResponse conversationsListResponse = new ConversationsListResponse();
        conversationsListResponse.setOk(true);
        conversationsListResponse.setChannels(new ArrayList<>());
        return conversationsListResponse;
    }

    @DisplayName("유효한 토큰을 검증한다.")
    @Test
    void verifyToken() {
        // given
        String token = jwtTokenProvider.createToken("1");

        // when & then
        assertDoesNotThrow(() -> authService.verifyToken(token));
    }

    @DisplayName("유효하지 않은 토큰을 검증한다.")
    @Test
    void verifyInvalidToken() {
        // given
        String token = "invalid token";

        // when & then
        assertThatThrownBy(() -> authService.verifyToken(token))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("만료된 토큰을 검증한다.")
    @Test
    void verifyExpiredToken() {
        // given
        JwtTokenProvider expiredJwtTokenProvider = new JwtTokenProvider(secretKey, 0);
        String token = expiredJwtTokenProvider.createToken("1");

        // when & then
        assertThatThrownBy(() -> authService.verifyToken(token))
                .isInstanceOf(ExpiredTokenException.class);
    }

    @DisplayName("시그니처가 다른 토큰을 검증한다.")
    @Test
    void verifyDifferentSignatureToken() {
        // given
        JwtTokenProvider otherJwtTokenProvider = new JwtTokenProvider("another secret key 123467891236789231", 600000);
        String token = otherJwtTokenProvider.createToken("1");

        // when & then
        assertThatThrownBy(() -> authService.verifyToken(token))
                .isInstanceOf(InvalidTokenException.class);
    }
}
