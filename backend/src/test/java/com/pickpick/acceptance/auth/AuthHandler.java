package com.pickpick.acceptance.auth;

import com.pickpick.acceptance.RestHandler;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("NonAsciiCharacters")
@Component
public class AuthHandler {

    private static final String LOGIN_API_URL = "/api/slack-login";
    private static final String CERTIFICATION_API_URL = "/api/certification";
    private static final String SLACK_EVENT_API_URL = "/api/event";

    @Autowired
    private RestHandler restHandler;

    public void 회원가입(final String slackId) {
        Map<String, Object> request = Map.of(
                "event", Map.of(
                        "type", "team_join",
                        "user", Map.of(
                                "id", slackId,
                                "profile", Map.of(
                                        "real_name", "봄",
                                        "display_name", "가을",
                                        "image_48", "bom.png"
                                )
                        )
                ));

        ExtractableResponse<Response> response = restHandler.post(SLACK_EVENT_API_URL, request);
        restHandler.상태코드_200_확인(response);
    }

    public ExtractableResponse<Response> 로그인(final String code) {
        Map<String, Object> request = Map.of("code", code);
        return restHandler.get(LOGIN_API_URL, request);
    }

    public ExtractableResponse<Response> 토큰_검증(final long memberId) {
        return restHandler.getWithCreateToken(CERTIFICATION_API_URL, memberId);
    }

    public ExtractableResponse<Response> 토큰_검증(final String token) {
        return restHandler.getWithToken(CERTIFICATION_API_URL, token);
    }
}
