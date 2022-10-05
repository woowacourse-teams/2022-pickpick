package com.pickpick.acceptance.auth;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_400_확인;
import static com.pickpick.acceptance.RestHandler.에러코드_확인;
import static com.pickpick.acceptance.auth.AuthRestHandler.로그인;
import static com.pickpick.acceptance.auth.AuthRestHandler.토큰_검증;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.회원가입;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.workspace.domain.Workspace;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse.AuthedUser;
import com.slack.api.methods.response.users.UsersIdentityResponse;
import com.slack.api.methods.response.users.UsersIdentityResponse.User;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("인증 & 인가 인수 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    private static final Workspace workspace = new Workspace("T12345",
            "xoxb-token-1234"); // TODO workspace 등록 API 생기면 제거 필요

    @Test
    void 정상_로그인() {
        // given
        String memberSlackId = "U03MC231";
        워크스페이스_등록(workspace);
        회원가입(memberSlackId, workspace.getSlackId());

        // when
        ExtractableResponse<Response> response = 로그인(memberSlackId);

        // then
        상태코드_200_확인(response);
        응답_바디에_토큰_존재(response);
    }

    @Test
    void 유효한_토큰_검증() {
        // given
        String token = jwtTokenProvider.createToken("2");

        // when
        ExtractableResponse<Response> response = 토큰_검증(token);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 유효하지_않은_토큰_검증_시_예외_처리() {
        // given
        String invalidToken = "abcde12345";

        // when
        ExtractableResponse<Response> response = 토큰_검증(invalidToken);

        // then
        상태코드_400_확인(response);
        에러코드_확인(response, "INVALID_TOKEN");
    }

    @Test
    void 만료된_토큰_검증_시_예외_처리() {
        // given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey, 0);
        String invalidToken = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 토큰_검증(invalidToken);

        // then
        상태코드_400_확인(response);
    }

    @Test
    void 시그니처가_다른_토큰_검증_시_예외_처리() {
        // given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("other" + secretKey, 60000);
        String invalidToken = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 토큰_검증(invalidToken);

        // then
        상태코드_400_확인(response);
        에러코드_확인(response, "INVALID_TOKEN");
    }

    private OAuthV2AccessResponse generateOAuthV2AccessResponse() {
        OAuthV2AccessResponse response = new OAuthV2AccessResponse();
        AuthedUser authedUser = new AuthedUser();
        authedUser.setAccessToken("token");
        response.setAuthedUser(authedUser);
        response.setOk(true);
        return response;
    }

    private UsersIdentityResponse generateUsersIdentityResponse(final String slackId) {
        UsersIdentityResponse usersIdentityResponse = new UsersIdentityResponse();
        User user = new User();
        user.setId(slackId);
        usersIdentityResponse.setUser(user);
        usersIdentityResponse.setOk(true);
        return usersIdentityResponse;
    }

    private void 응답_바디에_토큰_존재(final ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("token")).isNotBlank();
    }
}
