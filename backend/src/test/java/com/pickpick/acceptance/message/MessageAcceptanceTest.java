package com.pickpick.acceptance.message;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.channel.ChannelRestHandler.채널_구독_요청;
import static com.pickpick.acceptance.message.BookmarkRestHandler.북마크_생성;
import static com.pickpick.acceptance.message.MessageRestHandler.메시지_조회;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_생성;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_목록_생성;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.빈_메시지_전송;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.채널_생성_후_메시지_저장;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.키워드를_포함한_메시지_목록_생성;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.회원가입;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTest;
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
class MessageAcceptanceTest extends AcceptanceTest {

    private static final String MEMBER_SLACK_ID = "U00001234";

    private String token;

    @BeforeEach
    void init() {
        회원가입(MEMBER_SLACK_ID);
        token = jwtTokenProvider.createToken("1");
    }

    @Test
    void 텍스트가_비었으면_메시지_조회_시_필터링_됨() {
        // given
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());
        메시지_목록_생성(MEMBER_SLACK_ID, 10);
        빈_메시지_전송(MEMBER_SLACK_ID);
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
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());
        메시지_목록_생성(MEMBER_SLACK_ID, 21);
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
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());
        메시지_목록_생성(MEMBER_SLACK_ID, 2);
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
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());
        메시지_목록_생성(MEMBER_SLACK_ID, 21);
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
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());
        메시지_목록_생성(MEMBER_SLACK_ID, 11);
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
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());
        채널_구독_요청(token, 1L);

        int messageCount = 5;
        키워드를_포함한_메시지_목록_생성(MEMBER_SLACK_ID, messageCount, keyword);

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
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.NOTICE.create());
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.FREE_CHAT.create());
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());

        long[] channelIds = new long[]{1L, 2L};
        MessageRequestBuilder request = new MessageRequestBuilder()
                .channelIds(channelIds);

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(메시지_개수(response)).isEqualTo(channelIds.length);
    }

    @Test
    void 메시지_조회_시_작성_시간_기준_내림차순으로_조회() {
        // given
        메시지_목록_생성(MEMBER_SLACK_ID, 5);
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
        메시지_목록_생성(MEMBER_SLACK_ID, 25);
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
        메시지_목록_생성(MEMBER_SLACK_ID, messageCount + 5);
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
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());
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
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());
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
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());
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
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());
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
        메시지_목록_생성(MEMBER_SLACK_ID, 5);
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
        메시지_목록_생성(MEMBER_SLACK_ID, 5);
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

    private List<Long> 메시지_ID_목록(ExtractableResponse<Response> response) {
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

    private MessageResponses toMessageResponses(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("", MessageResponses.class);
    }
}
