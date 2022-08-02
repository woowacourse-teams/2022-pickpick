package com.pickpick.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.pickpick.auth.ui.dto.LoginRequest;
import com.pickpick.exception.PermissionDeniedException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.oauth.OAuthV2AccessRequest;
import com.slack.api.methods.request.users.UsersIdentityRequest;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse.AuthedUser;
import com.slack.api.methods.response.users.UsersIdentityResponse;
import com.slack.api.methods.response.users.UsersIdentityResponse.User;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private MethodsClient slackClient;

    @Autowired
    private MemberRepository members;

    @DisplayName("로그인")
    @Test
    void login() throws SlackApiException, IOException {
        // given
        Member member = new Member("slackId", "username", "thumbnail.png");
        members.save(member);

        given(slackClient.oauthV2Access(any(OAuthV2AccessRequest.class)))
                .willReturn(generateOAuthV2AccessResponse());
        given(slackClient.usersIdentity(any(UsersIdentityRequest.class)))
                .willReturn(generateUsersIdentityResponse(member.getSlackId()));

        // when
        String jupjupToken = authService.login(new LoginRequest(null, "code1234"));

        // then
        assertThat(jupjupToken).isNotBlank();
    }

    @DisplayName("error가 access_denied인 경우 로그인 실패")
    @Test
    void loginFailed() throws SlackApiException, IOException {
        // given
        Member member = new Member("slackId", "username", "thumbnail.png");
        members.save(member);

        given(slackClient.oauthV2Access(any(OAuthV2AccessRequest.class)))
                .willReturn(generateOAuthV2AccessResponse());
        given(slackClient.usersIdentity(any(UsersIdentityRequest.class)))
                .willReturn(generateUsersIdentityResponse(member.getSlackId()));

        // when & then
        assertThatThrownBy(() -> authService.login(new LoginRequest("access_denied", null)))
                .isInstanceOf(PermissionDeniedException.class);
    }

    private OAuthV2AccessResponse generateOAuthV2AccessResponse() {
        OAuthV2AccessResponse response = new OAuthV2AccessResponse();
        AuthedUser authedUser = new AuthedUser();
        authedUser.setAccessToken("token");
        response.setAuthedUser(authedUser);
        return response;
    }

    private UsersIdentityResponse generateUsersIdentityResponse(final String slackId) {
        UsersIdentityResponse usersIdentityResponse = new UsersIdentityResponse();
        User user = new User();
        user.setId(slackId);
        usersIdentityResponse.setUser(user);
        return usersIdentityResponse;
    }
}
