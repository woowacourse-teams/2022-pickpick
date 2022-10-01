package com.pickpick.acceptance.auth;

import static com.pickpick.acceptance.RestHandler.get;
import static com.pickpick.acceptance.RestHandler.getWithToken;
import static com.pickpick.acceptance.RestHandler.post;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class AuthRestHandler {

    private static final String LOGIN_API_URL = "/api/slack-login";
    private static final String CERTIFICATION_API_URL = "/api/certification";
    private static final String SLACK_EVENT_API_URL = "/api/event";

    public static void 회원가입(final String slackId) {
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

        post(SLACK_EVENT_API_URL, request);
    }

    public static ExtractableResponse<Response> 로그인(final String code) {
        Map<String, Object> request = Map.of("code", code);
        return get(LOGIN_API_URL, request);
    }

    public static ExtractableResponse<Response> 토큰_검증(final String token) {
        return getWithToken(CERTIFICATION_API_URL, token);
    }
}
