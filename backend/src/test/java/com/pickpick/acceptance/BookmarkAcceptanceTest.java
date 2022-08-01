package com.pickpick.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/truncate.sql", "/bookmark.sql"})
@DisplayName("북마크 기능")
@SuppressWarnings("NonAsciiCharacters")
public class BookmarkAcceptanceTest extends AcceptanceTest {

    private static final String API_BOOKMARK = "/api/bookmarks";

    @Test
    void 북마크_생성() {
        // given & when
        ExtractableResponse<Response> response = postWithAuth(API_BOOKMARK, Map.of("messageId", 1), 1L);

        // then
        상태코드_확인(response, HttpStatus.CREATED);
    }
}
