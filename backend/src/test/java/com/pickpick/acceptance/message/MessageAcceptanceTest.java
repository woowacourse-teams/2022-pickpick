package com.pickpick.acceptance.message;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.channel.ChannelRestHandler.채널_구독_요청;
import static com.pickpick.acceptance.message.BookmarkRestHandler.북마크_생성;
import static com.pickpick.acceptance.message.MessageRestHandler.메시지_조회;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_생성;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_목록_생성;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_전송;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.빈_메시지_전송;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.키워드를_포함한_메시지_목록_생성;
import static com.pickpick.fixture.MemberFixture.KKOJAE;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTestBase;
import com.pickpick.acceptance.auth.AuthRestHandler;
import com.pickpick.acceptance.message.MessageRestHandler.MessageRequestBuilder;
import com.pickpick.fixture.ChannelFixture;
import com.pickpick.message.ui.dto.MessageResponse;
import com.pickpick.message.ui.dto.MessageResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메시지 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
class MessageAcceptanceTest extends AcceptanceTestBase {

    private String token;
    private String memberSlackId;

    @BeforeEach
    void init() {
        String code = 슬랙에서_코드_발행(KKOJAE);
        ExtractableResponse<Response> loginResponse = AuthRestHandler.워크스페이스_초기화(code);

        token = 로그인_응답에서_토큰_추출(loginResponse);
        memberSlackId = 코드로_멤버의_slackId_추출(code);
    }

    @Test
    void 텍스트가_비었으면_메시지_조회_시_필터링_됨() {
        // given
        메시지_목록_생성(memberSlackId, 10);
        빈_메시지_전송(memberSlackId);
        채널_구독_요청(token, 1L);

        MessageRequestBuilder request = new MessageRequestBuilder();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(메시지_ID_목록(response)).doesNotContain(12L);
    }

    @Test
    void 조회할_과거_메시지가_있으면_hasPast가_true() {
        // given
        메시지_목록_생성(memberSlackId, 21);
        채널_구독_요청(token, 1L);

        MessageRequestBuilder request = new MessageRequestBuilder();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(toMessageResponses(response).hasPast()).isTrue();
    }

    @Test
    void 조회할_과거_메시지가_없으면_hasPast가_false() {
        // given
        메시지_목록_생성(memberSlackId, 2);
        채널_구독_요청(token, 1L);

        MessageRequestBuilder request = new MessageRequestBuilder();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(toMessageResponses(response).hasPast()).isFalse();
    }

    @Test
    void 조회할_미래_메시지가_있으면_hasFuture가_true() {
        // given
        메시지_목록_생성(memberSlackId, 21);
        채널_구독_요청(token, 1L);

        MessageRequestBuilder request = new MessageRequestBuilder()
                .messageId(3L);

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(toMessageResponses(response).hasFuture()).isTrue();
    }

    @Test
    void 조회할_미래_메시지가_없으면_hasFuture가_false() {
        // given
        메시지_목록_생성(memberSlackId, 11);
        채널_구독_요청(token, 1L);

        MessageRequestBuilder request = new MessageRequestBuilder();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(toMessageResponses(response).hasFuture()).isFalse();
    }

    @Test
    void 키워드_검색() {
        // given
        String keyword = "줍줍";
        메시지_전송(memberSlackId);
        채널_구독_요청(token, 1L);

        int messageCount = 5;
        키워드를_포함한_메시지_목록_생성(memberSlackId, messageCount, keyword);
        메시지_목록_생성(memberSlackId, 3);

        MessageRequestBuilder request = new MessageRequestBuilder()
                .keyword(keyword);

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(메시지_개수(response)).isEqualTo(messageCount);
    }

    @Test
    void 특정_채널_목록에서_조회() {
        // given
        메시지_전송(memberSlackId, ChannelFixture.NOTICE);
        메시지_전송(memberSlackId, ChannelFixture.FREE_CHAT);

        List<Long> channelIds = List.of(1L, 2L);
        MessageRequestBuilder request = new MessageRequestBuilder()
                .channelIds(channelIds);

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(메시지_개수(response)).isEqualTo(channelIds.size());
    }

    @Test
    void 메시지_조회_시_작성_시간_기준_내림차순으로_조회() {
        // given
        메시지_목록_생성(memberSlackId, 5);
        채널_구독_요청(token, 1L);

        MessageRequestBuilder request = new MessageRequestBuilder();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(메시지_ID_목록(response)).isEqualTo(List.of(5L, 4L, 3L, 2L, 1L));
    }

    @Test
    void count_값이_없으면_기본으로_20개_조회() {
        // given
        메시지_목록_생성(memberSlackId, 25);
        채널_구독_요청(token, 1L);

        MessageRequestBuilder request = new MessageRequestBuilder();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(메시지_개수(response)).isEqualTo(20);
    }

    @Test
    void count_값이_있으면_해당_값만큼_메시지_조회() {
        // given
        int messageCount = 5;
        메시지_목록_생성(memberSlackId, messageCount + 5);
        채널_구독_요청(token, 1L);

        MessageRequestBuilder request = new MessageRequestBuilder()
                .messageCount(messageCount);

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(메시지_개수(response)).isEqualTo(messageCount);
    }

    @Test
    void 북마크한_메시지는_isBookmarked가_true() {
        // given
        메시지_전송(memberSlackId);
        채널_구독_요청(token, 1L);
        북마크_생성(token, 1L);

        MessageRequestBuilder request = new MessageRequestBuilder();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(북마크_여부(response)).isTrue();
    }

    @Test
    void 북마크하지_않은_메시지는_isBookmarked가_false() {
        // given
        메시지_전송(memberSlackId);
        채널_구독_요청(token, 1L);

        MessageRequestBuilder request = new MessageRequestBuilder();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(북마크_여부(response)).isFalse();
    }

    @Test
    void 리마인드한_메시지는_isSetReminded가_true() {
        // given
        메시지_전송(memberSlackId);
        채널_구독_요청(token, 1L);
        리마인더_생성(token, 1L, LocalDateTime.now().plusDays(1));

        MessageRequestBuilder request = new MessageRequestBuilder();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(리마인드_여부(response)).isTrue();
    }

    @Test
    void 리마인드하지_않은_메시지는_isSetReminded가_false() {
        // given
        메시지_전송(memberSlackId);
        채널_구독_요청(token, 1L);

        MessageRequestBuilder request = new MessageRequestBuilder();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(리마인드_여부(response)).isFalse();
    }

    @Test
    void 메시지_ID_3번이고_needPastMessage가_false인_경우_해당_메시지보다_미래_메시지를_조회() {
        // given
        메시지_목록_생성(memberSlackId, 5);
        채널_구독_요청(token, 1L);

        MessageRequestBuilder request = new MessageRequestBuilder()
                .messageId(3L)
                .needPastMessage(false);

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(메시지_ID_목록(response)).isEqualTo(List.of(5L, 4L));
    }

    @Test
    void 메시지_ID_3번이고_needPastMessage가_true인_경우_해당_메시지보다_과거_메시지를_조회() {
        // given
        메시지_목록_생성(memberSlackId, 5);
        채널_구독_요청(token, 1L);

        MessageRequestBuilder request = new MessageRequestBuilder()
                .messageId(3L)
                .needPastMessage(true);

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(메시지_ID_목록(response)).isEqualTo(List.of(2L, 1L));
    }

    private List<Long> 메시지_ID_목록(final ExtractableResponse<Response> response) {
        return toMessageResponses(response)
                .getMessages()
                .stream()
                .map(MessageResponse::getId)
                .collect(Collectors.toList());
    }

    private int 메시지_개수(final ExtractableResponse<Response> response) {
        return toMessageResponses(response).getMessages().size();
    }

    private boolean 북마크_여부(final ExtractableResponse<Response> response) {
        return toMessageResponses(response).getMessages()
                .get(0)
                .isBookmarked();
    }

    private boolean 리마인드_여부(final ExtractableResponse<Response> response) {
        return toMessageResponses(response).getMessages()
                .get(0)
                .isSetReminded();
    }

    private MessageResponses toMessageResponses(final ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("", MessageResponses.class);
    }
}
