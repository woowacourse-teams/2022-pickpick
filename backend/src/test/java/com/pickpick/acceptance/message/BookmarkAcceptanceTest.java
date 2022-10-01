package com.pickpick.acceptance.message;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.config.dto.ErrorResponse;
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

@Sql({"/bookmark.sql"})
@DisplayName("북마크 기능")
@SuppressWarnings("NonAsciiCharacters")
public class BookmarkAcceptanceTest extends AcceptanceTest {

    private static final String BOOKMARK_API_URL = "/api/bookmarks";

    @Test
    void 북마크_생성() {
        // given & when
        ExtractableResponse<Response> response = postWithCreateToken(BOOKMARK_API_URL, Map.of("messageId", 1), 1L);

        // then
        상태코드_확인(response, HttpStatus.CREATED);
    }

    @Test
    void 멤버_ID_2번으로_북마크_조회() {
        // given
        Map<String, Object> request = Map.of("bookmarkId", "");
        List<Long> expectedIds = List.of(1L);

        // when
        ExtractableResponse<Response> response = getWithCreateToken(BOOKMARK_API_URL, 2L, request);

        // then
        상태코드_확인(response, HttpStatus.OK);

        BookmarkResponses bookmarkResponses = response.jsonPath().getObject("", BookmarkResponses.class);
        assertThat(bookmarkResponses.hasPast()).isFalse();
        assertThat(convertToIds(bookmarkResponses)).containsExactlyElementsOf(expectedIds);
    }

    @Test
    void 멤버_ID_1번이고_북마크_ID가_23번일_때_북마크_목록_조회() {
        // given
        Map<String, Object> request = Map.of("bookmarkId", "23");
        List<Long> expectedIds = List.of(22L, 21L, 20L, 19L, 18L, 17L, 16L, 15L, 14L, 13L, 12L, 11L, 10L, 9L, 8L, 7L,
                6L, 5L, 4L, 3L);
        boolean expectedHasPast = true;

        // when
        ExtractableResponse<Response> response = getWithCreateToken(BOOKMARK_API_URL, 1L, request);

        // then
        상태코드_확인(response, HttpStatus.OK);

        BookmarkResponses bookmarkResponses = response.jsonPath().getObject("", BookmarkResponses.class);
        assertThat(bookmarkResponses.hasPast()).isEqualTo(expectedHasPast);
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
        long messageId = 2L;

        // when
        ExtractableResponse<Response> response = deleteWithCreateToken(BOOKMARK_API_URL + "?messageId=" + messageId,
                1L);

        // then
        상태코드_확인(response, HttpStatus.NO_CONTENT);
    }

    @Test
    void 사용자에게_존재하지_않는_북마크_삭제() {
        // given
        long messageId = 1L;

        // when
        ExtractableResponse<Response> response = deleteWithCreateToken(BOOKMARK_API_URL + "?messageId=" + messageId,
                1L);

        // then
        상태코드_확인(response, HttpStatus.BAD_REQUEST);
        assertThat(response.jsonPath().getObject("", ErrorResponse.class).getCode()).isEqualTo(
                "BOOKMARK_DELETE_FAILURE");
    }
}
