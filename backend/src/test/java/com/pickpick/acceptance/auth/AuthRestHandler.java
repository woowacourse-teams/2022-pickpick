package com.pickpick.acceptance.auth;

import static com.pickpick.acceptance.RestHandler.get;
import static com.pickpick.acceptance.RestHandler.getWithToken;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class AuthRestHandler {

    private static final String LOGIN_API_URL = "/api/slack-login";
    private static final String CERTIFICATION_API_URL = "/api/certification";
    private static final String WORKSPACE_API_URL = "/api/slack-workspace";

    public static ExtractableResponse<Response> 로그인(final String code) {
        Map<String, Object> request = Map.of("code", code);
        return get(LOGIN_API_URL, request);
    }

    public static ExtractableResponse<Response> 토큰_검증(final String token) {
        return getWithToken(CERTIFICATION_API_URL, token);
    }

    public static ExtractableResponse<Response> 워크스페이스_초기화(final String code) {
        Map<String, Object> request = Map.of("code", code);
        return get(WORKSPACE_API_URL, request);
    }
}
