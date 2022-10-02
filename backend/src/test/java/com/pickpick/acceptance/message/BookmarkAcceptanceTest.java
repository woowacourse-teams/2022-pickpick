package com.pickpick.acceptance.message;

import static com.pickpick.acceptance.RestHandler.상태코드_확인;
import static com.pickpick.acceptance.RestHandler.에러코드_확인;
import static com.pickpick.acceptance.message.BookmarkRestHandler.북마크_삭제;
import static com.pickpick.acceptance.message.BookmarkRestHandler.북마크_생성;
import static com.pickpick.acceptance.message.BookmarkRestHandler.북마크_조회;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.message.ui.dto.BookmarkResponse;
import com.pickpick.message.ui.dto.BookmarkResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/bookmark.sql"})
@DisplayName("북마크 기능")
@SuppressWarnings("NonAsciiCharacters")
public class BookmarkAcceptanceTest extends AcceptanceTest {

    @Test
    void 북마크_생성_검증() {
        // given
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 북마크_생성(token, 1L);

        // then
        상태코드_확인(response, HttpStatus.CREATED);
    }

    @Test
    void 멤버_ID_2번으로_북마크_조회() {
        // given
        String token = jwtTokenProvider.createToken("2");
        List<Long> expectedIds = List.of(1L);
        boolean expectedHasPast = false;

        // when
        ExtractableResponse<Response> response = 북마크_조회(token, null);

        // then
        상태코드_확인(response, HttpStatus.OK);

        BookmarkResponses bookmarkResponses = response.jsonPath().getObject("", BookmarkResponses.class);
        assertThat(bookmarkResponses.hasPast()).isFalse();
        assertThat(convertToIds(bookmarkResponses)).containsExactlyElementsOf(expectedIds);
    }

    @Test
    void 멤버_ID_1번이고_북마크_ID가_23번일_때_북마크_목록_조회() {
        // given
        String token = jwtTokenProvider.createToken("1");
        List<Long> expectedIds = List.of(22L, 21L, 20L, 19L, 18L, 17L, 16L, 15L, 14L, 13L, 12L, 11L, 10L, 9L, 8L, 7L,
                6L, 5L, 4L, 3L);
        boolean expectedHasPast = true;

        // when
        ExtractableResponse<Response> response = 북마크_조회(token, 23L);

        // then
        상태코드_확인(response, HttpStatus.OK);

        BookmarkResponses bookmarkResponses = response.jsonPath().getObject("", BookmarkResponses.class);
        assertThat(bookmarkResponses.hasPast()).isTrue();
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
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 북마크_삭제(token, 2L);

        // then
        상태코드_확인(response, HttpStatus.NO_CONTENT);
    }

    @Test
    void 사용자에게_존재하지_않는_북마크_삭제() {
        // given
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 북마크_삭제(token, 1L);

        // then
        상태코드_확인(response, HttpStatus.BAD_REQUEST);
        에러코드_확인(response, "BOOKMARK_DELETE_FAILURE");
    }
}
