package com.pickpick.acceptance.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.auth.support.JwtTokenProvider;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.oauth.OAuthV2AccessRequest;
import com.slack.api.methods.request.users.UsersIdentityRequest;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse.AuthedUser;
import com.slack.api.methods.response.users.UsersIdentityResponse;
import com.slack.api.methods.response.users.UsersIdentityResponse.User;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("인증 & 인가 인수 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String MEMBER_SLACK_ID = "U03MC231";

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Autowired
    private AuthHandler authHandler;

    @Test
    void 정상_로그인() throws SlackApiException, IOException {
        // given
        given(slackClient.oauthV2Access(any(OAuthV2AccessRequest.class)))
                .willReturn(generateOAuthV2AccessResponse());
        given(slackClient.usersIdentity(any(UsersIdentityRequest.class)))
                .willReturn(generateUsersIdentityResponse());

        authHandler.회원가입(MEMBER_SLACK_ID);

        // when
        ExtractableResponse<Response> response = authHandler.로그인("1234");

        // then
        restHandler.상태코드_200_확인(response);
        응답_바디에_토큰_존재(response);
    }

    private OAuthV2AccessResponse generateOAuthV2AccessResponse() {
        OAuthV2AccessResponse response = new OAuthV2AccessResponse();
        AuthedUser authedUser = new AuthedUser();
        authedUser.setAccessToken("token");
        response.setAuthedUser(authedUser);
        return response;
    }

    private UsersIdentityResponse generateUsersIdentityResponse() {
        UsersIdentityResponse usersIdentityResponse = new UsersIdentityResponse();
        User user = new User();
        user.setId(MEMBER_SLACK_ID);
        usersIdentityResponse.setUser(user);
        return usersIdentityResponse;
    }

    private void 응답_바디에_토큰_존재(final ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("token")).isNotBlank();
    }

    @Test
    void 유효한_토큰_검증() {
        // given & when
        ExtractableResponse<Response> response = authHandler.토큰_검증(2L);

        // then
        restHandler.상태코드_200_확인(response);
    }

    @Test
    void 유효하지_않은_토큰_검증() {
        // given
        String invalidToken = "abcde12345";

        // when
        ExtractableResponse<Response> response = authHandler.토큰_검증(invalidToken);

        // then
        restHandler.상태코드_400_확인(response);
        assertThat(restHandler.에러_코드(response)).isEqualTo("INVALID_TOKEN");
    }

    @Test
    void 만료된_토큰_검증() {
        // given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey, 0);
        String invalidToken = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = authHandler.토큰_검증(invalidToken);

        // then
        restHandler.상태코드_400_확인(response);
    }

    @Test
    void 시그니처가_다른_토큰_검증() {
        // given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("other" + secretKey, 60000);
        String invalidToken = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = authHandler.토큰_검증(invalidToken);

        // then
        restHandler.상태코드_400_확인(response);
        assertThat(restHandler.에러_코드(response)).isEqualTo("INVALID_TOKEN");
    }
}
