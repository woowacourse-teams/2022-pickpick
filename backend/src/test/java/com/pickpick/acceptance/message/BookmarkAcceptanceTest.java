package com.pickpick.acceptance.message;

import static com.pickpick.acceptance.RestHandler.상태코드_확인;
import static com.pickpick.acceptance.RestHandler.에러코드_확인;
import static com.pickpick.acceptance.message.BookmarkRestHandler.북마크_삭제;
import static com.pickpick.acceptance.message.BookmarkRestHandler.북마크_생성;
import static com.pickpick.acceptance.message.BookmarkRestHandler.북마크_조회;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_전송;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.채널_생성;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.채널_생성_후_메시지_저장;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.회원가입;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.fixture.ChannelFixture;
import com.pickpick.message.ui.dto.BookmarkResponse;
import com.pickpick.message.ui.dto.BookmarkResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("북마크 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
public class BookmarkAcceptanceTest extends AcceptanceTest {

    private static final String MEMBER_SLACK_ID = "MB1234";
    private static final String MEMBER_SLACK_ID_2 = "MB1235";

    @Test
    void 북마크_생성_검증() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.NOTICE.create());

        // when
        ExtractableResponse<Response> response = 북마크_생성(token, 1L);

        // then
        상태코드_확인(response, HttpStatus.CREATED);
    }

    @Test
    void 멤버_ID_1번으로_북마크_조회() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");

        채널_생성(MEMBER_SLACK_ID, ChannelFixture.QNA.create());
        메시지_전송(MEMBER_SLACK_ID, "M1");
        메시지_전송(MEMBER_SLACK_ID, "M2");
        메시지_전송(MEMBER_SLACK_ID, "M3");

        북마크_생성(token, 1L);
        북마크_생성(token, 2L);

        // when
        ExtractableResponse<Response> response = 북마크_조회(token, null);

        // then
        상태코드_확인(response, HttpStatus.OK);

        BookmarkResponses bookmarkResponses = response.jsonPath().getObject("", BookmarkResponses.class);
        assertThat(bookmarkResponses.hasPast()).isFalse();
        assertThat(convertToMessageIds(bookmarkResponses)).containsExactlyElementsOf(List.of(1L, 2L));
    }

    @Test
    void 멤버_ID_1번이고_북마크_ID가_23번일_때_북마크_목록_조회() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");

        int messageCount = 10;
        메시지_목록_생성(messageCount);

        List<Long> messageIdsForBookmark = List.of(1L, 3L, 5L, 7L);
        북마크_목록_생성(token, messageIdsForBookmark);

        // when
        ExtractableResponse<Response> response = 북마크_조회(token, null);

        // then
        상태코드_확인(response, HttpStatus.OK);

        BookmarkResponses bookmarkResponses = response.jsonPath().getObject("", BookmarkResponses.class);
        assertThat(bookmarkResponses.hasPast()).isFalse();
        assertThat(convertToMessageIds(bookmarkResponses)).containsExactlyElementsOf(messageIdsForBookmark);
    }

    private void 메시지_목록_생성(final int count) {
        for (int i = 1; i <= count; i++) {
            메시지_전송(MEMBER_SLACK_ID, "MSG_SLACK_ID" + i);
        }
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

    @Test
    void 북마크_정상_삭제() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");

        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.NOTICE.create());
        long messageId = 1L;
        북마크_생성(token, messageId);

        // when
        ExtractableResponse<Response> response = 북마크_삭제(token, messageId);

        // then
        상태코드_확인(response, HttpStatus.NO_CONTENT);
    }

    @Test
    void 사용자에게_존재하지_않는_북마크_삭제() {
        // given
        회원가입(MEMBER_SLACK_ID);
        회원가입(MEMBER_SLACK_ID_2);
        String token1 = jwtTokenProvider.createToken("1");
        String token2 = jwtTokenProvider.createToken("2");

        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.NOTICE.create());
        북마크_생성(token2, 1L);

        // when
        ExtractableResponse<Response> response = 북마크_삭제(token1, 1L);

        // then
        상태코드_확인(response, HttpStatus.BAD_REQUEST);
        에러코드_확인(response, "BOOKMARK_DELETE_FAILURE");
    }
}
