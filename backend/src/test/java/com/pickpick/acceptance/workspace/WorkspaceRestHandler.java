package com.pickpick.acceptance.workspace;

import static com.pickpick.acceptance.RestHandler.get;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class WorkspaceRestHandler {

    private static final String WORKSPACE_API_URL = "/api/slack-workspace";

    public static ExtractableResponse<Response> 워크스페이스_초기화(final String code) {
        Map<String, Object> request = Map.of("code", code);
        return get(WORKSPACE_API_URL, request);
    }
}
