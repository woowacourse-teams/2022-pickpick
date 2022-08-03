package com.pickpick.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.message.ui.dto.BookmarkResponse;
import com.pickpick.message.ui.dto.BookmarkResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        ExtractableResponse<Response> response = postWithCreateToken(API_BOOKMARK, Map.of("messageId", 1), 1L);

        // then
        상태코드_확인(response, HttpStatus.CREATED);
    }

    @Test
    void 멤버_ID_2번으로_북마크_조회() {
        // given
        Map<String, Object> request = Map.of("bookmarkId", "");
        List<Long> expectedIds = List.of(1L);
        boolean expectedIsLast = true;

        // when
        ExtractableResponse<Response> response = getWithCreateToken(API_BOOKMARK, 2L, request);

        // then
        상태코드_확인(response, HttpStatus.OK);

        BookmarkResponses bookmarkResponses = response.jsonPath().getObject("", BookmarkResponses.class);
        assertThat(bookmarkResponses.isLast()).isEqualTo(expectedIsLast);
        assertThat(convertToIds(bookmarkResponses)).containsExactlyElementsOf(expectedIds);
    }

    @Test
    void 멤버_ID_1번이고_북마크_ID가_23번일_때_북마크_목록_조회() {
        // given
        Map<String, Object> request = Map.of("bookmarkId", "23");
        List<Long> expectedIds = List.of(22L, 21L, 20L, 19L, 18L, 17L, 16L, 15L, 14L, 13L, 12L, 11L, 10L, 9L, 8L, 7L,
                6L, 5L, 4L, 3L);
        boolean expectedIsLast = false;

        // when
        ExtractableResponse<Response> response = getWithCreateToken(API_BOOKMARK, 1L, request);

        // then
        상태코드_확인(response, HttpStatus.OK);

        BookmarkResponses bookmarkResponses = response.jsonPath().getObject("", BookmarkResponses.class);
        assertThat(bookmarkResponses.isLast()).isEqualTo(expectedIsLast);
        assertThat(convertToIds(bookmarkResponses)).containsExactlyElementsOf(expectedIds);
    }

    private List<Long> convertToIds(final BookmarkResponses response) {
        return response.getBookmarks()
                .stream()
                .map(BookmarkResponse::getId)
                .collect(Collectors.toList());
    }

    @Test
    void 북마크_정상_삭제() {
        // given
        long bookmarkId = 2L;

        // when
        ExtractableResponse<Response> response = deleteWithCreateToken(API_BOOKMARK + "/" + bookmarkId, 1L);

        // then
        상태코드_확인(response, HttpStatus.NO_CONTENT);
    }

    @Test
    void 다른_사용자의_북마크_삭제() {
        // given
        long bookmarkId = 1L;

        // when
        ExtractableResponse<Response> response = deleteWithCreateToken(API_BOOKMARK + "/" + bookmarkId, 1L);

        // then
        상태코드_확인(response, HttpStatus.BAD_REQUEST);
    }
}
