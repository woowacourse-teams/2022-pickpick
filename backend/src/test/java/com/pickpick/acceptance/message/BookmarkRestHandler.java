package com.pickpick.acceptance.message;

import static com.pickpick.acceptance.RestHandler.deleteWithToken;
import static com.pickpick.acceptance.RestHandler.getWithToken;
import static com.pickpick.acceptance.RestHandler.postWithToken;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class BookmarkRestHandler {

    private static final String BOOKMARK_API_URL = "/api/bookmarks";

    public static ExtractableResponse<Response> 북마크_생성(final String token, final long messageId) {
        return postWithToken(BOOKMARK_API_URL, Map.of("messageId", messageId), token);
    }

    public static ExtractableResponse<Response> 북마크_조회(final String token, final Long bookmarkId) {
        if (bookmarkId == null) {
            return getWithToken(BOOKMARK_API_URL, token);
        }
        return getWithToken(BOOKMARK_API_URL, token, Map.of("bookmarkId", bookmarkId));
    }

    public static ExtractableResponse<Response> 북마크_삭제(final String token, final long messageId) {
        return deleteWithToken(BOOKMARK_API_URL, token, Map.of("messageId", messageId));
    }
}
