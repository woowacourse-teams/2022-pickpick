package com.pickpick.acceptance.message;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_201_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_204_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_400_확인;
import static com.pickpick.acceptance.RestHandler.에러코드_확인;
import static com.pickpick.acceptance.message.BookmarkRestHandler.북마크_삭제;
import static com.pickpick.acceptance.message.BookmarkRestHandler.북마크_생성;
import static com.pickpick.acceptance.message.BookmarkRestHandler.북마크_조회;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_목록_생성;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_전송;
import static com.pickpick.acceptance.workspace.WorkspaceRestHandler.워크스페이스_초기화_및_로그인;
import static com.pickpick.fixture.MemberFixture.HOPE;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTestBase;
import com.pickpick.message.ui.dto.BookmarkResponse;
import com.pickpick.message.ui.dto.BookmarkResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("북마크 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
class BookmarkAcceptanceTest extends AcceptanceTestBase {

    private String token;
    private String memberSlackId;

    @BeforeEach
    void init() {
        String code = 슬랙에서_코드_발행(HOPE);
        ExtractableResponse<Response> loginResponse = 워크스페이스_초기화_및_로그인(code);

        token = 로그인_응답에서_토큰_추출(loginResponse);
        memberSlackId = 코드로_멤버의_slackId_추출(code);
    }

    @Test
    void 북마크_생성_검증() {
        // given
        메시지_전송(memberSlackId);

        // when
        ExtractableResponse<Response> response = 북마크_생성(token, 1L);

        // then
        상태코드_201_확인(response);
    }

    @Test
    void 특정_멤버가_북마크한_메시지_목록_조회() {
        // given

        메시지_목록_생성(memberSlackId, 3);
        북마크_생성(token, 1L);
        북마크_생성(token, 2L);

        // when
        ExtractableResponse<Response> response = 북마크_조회(token, null);

        // then
        상태코드_200_확인(response);

        BookmarkResponses bookmarkResponses = toBookmarkResponses(response);
        assertThat(bookmarkResponses.hasPast()).isFalse();
        assertThat(convertToMessageIds(bookmarkResponses)).containsExactlyElementsOf(List.of(2L, 1L));
    }

    @Test
    void 북마크_id로_조회할_경우_더_과거의_북마크들_아이디만_조회() {
        // given
        int messageCount = 10;
        메시지_목록_생성(memberSlackId, messageCount);

        List<Long> messageIdsForBookmark = List.of(1L, 3L, 5L, 7L);
        북마크_목록_생성(token, messageIdsForBookmark);

        // when
        ExtractableResponse<Response> response = 북마크_조회(token, 2L);

        // then
        상태코드_200_확인(response);

        BookmarkResponses bookmarkResponses = toBookmarkResponses(response);
        assertThat(bookmarkResponses.hasPast()).isFalse();
        assertThat(convertToMessageIds(bookmarkResponses)).containsExactlyElementsOf(List.of(1L));
    }

    @Test
    void 북마크_정상_삭제() {
        // given
        메시지_전송(memberSlackId);
        long messageId = 1L;
        북마크_생성(token, messageId);

        // when
        ExtractableResponse<Response> response = 북마크_삭제(token, messageId);

        // then
        상태코드_204_확인(response);
    }

    @Test
    void 사용자에게_존재하지_않는_북마크_삭제_시_400_응답() {
        // given
        String token1 = jwtTokenProvider.createToken("1");
        String token2 = jwtTokenProvider.createToken("2");

        메시지_전송(memberSlackId);
        북마크_생성(token2, 1L);

        // when
        ExtractableResponse<Response> response = 북마크_삭제(token1, 1L);

        // then
        상태코드_400_확인(response);
        에러코드_확인(response, "BOOKMARK_DELETE_FAILURE");
    }

    private BookmarkResponses toBookmarkResponses(final ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("", BookmarkResponses.class);
    }

    private void 북마크_목록_생성(final String token, final List<Long> messageIds) {
        for (Long messageId : messageIds) {
            북마크_생성(token, messageId);
        }
    }

    private List<Long> convertToMessageIds(final BookmarkResponses response) {
        return response.getBookmarks()
                .stream()
                .map(BookmarkResponse::getMessageId)
                .collect(Collectors.toList());
    }
}
