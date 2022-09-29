package com.pickpick.acceptance.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.config.dto.ErrorResponse;
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
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("인증 & 인가 인수 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String LOGIN_API_URL = "/api/slack-login";
    private static final String CERTIFICATION_API_URL = "/api/certification";
    private static final String MEMBER_SLACK_ID = "U03MC231";

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Test
    void 정상_로그인() throws SlackApiException, IOException {
        // given
        given(slackClient.oauthV2Access(any(OAuthV2AccessRequest.class)))
                .willReturn(generateOAuthV2AccessResponse());
        given(slackClient.usersIdentity(any(UsersIdentityRequest.class)))
                .willReturn(generateUsersIdentityResponse());

        회원가입();

        // when
        ExtractableResponse<Response> response = get(LOGIN_API_URL, Map.of("code", "1234"));

        // then
        상태코드_200_확인(response);
        응답_바디에_토큰_존재(response);
    }

    private void 회원가입() {
        Map<String, Object> request = Map.of(
                "event", Map.of(
                        "type", "team_join",
                        "user", Map.of(
                                "id", MEMBER_SLACK_ID,
                                "profile", Map.of(
                                        "real_name", "봄",
                                        "display_name", "가을",
                                        "image_48", "bom.png"
                                )
                        )
                ));

        ExtractableResponse<Response> response = post("/api/event", request);
        상태코드_200_확인(response);
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
        ExtractableResponse<Response> response = getWithCreateToken(CERTIFICATION_API_URL, 2L);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 유효하지_않은_토큰_검증() {
        // given
        String invalidToken = "abcde12345";

        // when
        ExtractableResponse<Response> response = getWithToken(CERTIFICATION_API_URL, invalidToken);

        // then
        상태코드_400_확인(response);
        assertThat(response.jsonPath().getObject("", ErrorResponse.class).getCode()).isEqualTo("INVALID_TOKEN");
    }

    @Test
    void 만료된_토큰_검증() {
        // given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey, 0);
        String invalidToken = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = getWithToken(CERTIFICATION_API_URL, invalidToken);

        // then
        상태코드_400_확인(response);
    }

    @Test
    void 시그니처가_다른_토큰_검증() {
        // given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("other" + secretKey, 60000);
        String invalidToken = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = getWithToken(CERTIFICATION_API_URL, invalidToken);

        // then
        상태코드_400_확인(response);
        assertThat(response.jsonPath().getObject("", ErrorResponse.class).getCode()).isEqualTo("INVALID_TOKEN");
    }
}
