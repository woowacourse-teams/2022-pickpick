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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@AutoConfigureMockMvc
@SpringBootTest
class AuthServiceTest {

    @MockBean
    private MethodsClient slackClient;

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository members;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @DisplayName("최초 로그인 시 토큰과 isFirstLogin true, 두번째 이후부턴 토큰과 false가 반환되어야 한다")
    @Test
    void login() throws SlackApiException, IOException {
        // given
        Member member = members.save(new Member("slackId", "username", "thumbnail.png"));

        given(slackClient.oauthV2Access(any(OAuthV2AccessRequest.class)))
                .willReturn(generateOAuthV2AccessResponse());
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
