package com.pickpick.acceptance.channel;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_400_확인;
import static com.pickpick.acceptance.RestHandler.에러코드_확인;
import static com.pickpick.acceptance.channel.ChannelRestHandler.구독_요청;
import static com.pickpick.acceptance.channel.ChannelRestHandler.구독_채널_순서_변경_요청;
import static com.pickpick.acceptance.channel.ChannelRestHandler.구독_취소_요청;
import static com.pickpick.acceptance.channel.ChannelRestHandler.유저_구독_채널_목록_조회_요청;
import static com.pickpick.acceptance.channel.ChannelRestHandler.유저_전체_채널_목록_조회_요청;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.채널_생성;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.회원가입;
import static com.pickpick.fixture.ChannelFixture.FREE_CHAT;
import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.channel.ui.dto.ChannelOrderRequest;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.channel.ui.dto.ChannelSubscriptionResponse;
import com.pickpick.workspace.domain.Workspace;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("채널 구독 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
class ChannelSubscriptionAcceptanceTest extends AcceptanceTest {

    private static final String MEMBER_SLACK_ID = "slackId123";

    private String token;
    private Workspace workspace;

    @BeforeEach
    void 가입_후_로그인() {
        workspace = 워크스페이스_등록(new Workspace("T12345", "xoxb-token-1234"));
        회원가입(MEMBER_SLACK_ID, workspace.getSlackId());
        token = jwtTokenProvider.createToken("1");
    }

    @Test
    void 채널_구독() {
        // given
        채널_생성(MEMBER_SLACK_ID, NOTICE.create(workspace));
        long noticeId = 1L;

        // when
        ExtractableResponse<Response> response = 구독_요청(token, noticeId);

        // then
        상태코드_200_확인(response);
        assertThat(구독한_채널_id_목록()).contains(noticeId);
    }

    @Test
    void 채널_구독_취소() {
        // given
        채널_생성(MEMBER_SLACK_ID, NOTICE.create(workspace));
        long noticeId = 1L;

        구독_요청(token, noticeId);

        // when
        ExtractableResponse<Response> response = 구독_취소_요청(token, noticeId);

        // then
        상태코드_200_확인(response);
        assertThat(구독한_채널_id_목록()).doesNotContain(noticeId);
    }

    @Test
    void 채널_구독_조회() {
        // given
        채널_생성(MEMBER_SLACK_ID, NOTICE.create(workspace));
        long noticeId = 1L;
        구독_요청(token, noticeId);

        채널_생성(MEMBER_SLACK_ID, FREE_CHAT.create(workspace));
        long freeChatId = 2L;
        구독_요청(token, freeChatId);

        // when
        ExtractableResponse<Response> response = 유저_구독_채널_목록_조회_요청(token);

        // then
        상태코드_200_확인(response);
        구독이_올바른_순서로_조회됨(response, noticeId, freeChatId);
    }

    @Test
    void 구독_채널_순서_변경() {
        // given
        채널_생성(MEMBER_SLACK_ID, NOTICE.create(workspace));
        long noticeId = 1L;
        구독_요청(token, noticeId);

        채널_생성(MEMBER_SLACK_ID, FREE_CHAT.create(workspace));
        long freeChatId = 2L;
        구독_요청(token, freeChatId);

        // when
        ExtractableResponse<Response> subscriptionOrderChangeResponse = 올바른_구독_채널_순서_변경_요청(token, noticeId, freeChatId);

        // then
        상태코드_200_확인(subscriptionOrderChangeResponse);

        ExtractableResponse<Response> subscriptionsResponse = 유저_구독_채널_목록_조회_요청(token);
        구독이_올바른_순서로_조회됨(subscriptionsResponse, freeChatId, noticeId);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 구독_채널_순서_변경_시_1보다_작은_순서가_들어올_경우_예외_발생(int invalidViewOrder) {
        // given
        채널_생성(MEMBER_SLACK_ID, NOTICE.create(workspace));
        long noticeId = 1L;
        구독_요청(token, noticeId);

        채널_생성(MEMBER_SLACK_ID, FREE_CHAT.create(workspace));
        long freeChatId = 2L;
        구독_요청(token, freeChatId);

        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(noticeId, invalidViewOrder),
                new ChannelOrderRequest(freeChatId, 1)
        );

        // when
        ExtractableResponse<Response> response = 구독_채널_순서_변경_요청(token, request);

        // then
        상태코드_400_확인(response);
        에러코드_확인(response, "SUBSCRIPTION_INVALID_ORDER");
    }

    @Test
    void 구독_채널_순서_변경_시_중복된_순서가_들어올_경우_예외_발생() {
        // given
        채널_생성(MEMBER_SLACK_ID, NOTICE.create(workspace));
        long noticeId = 1L;
        구독_요청(token, noticeId);

        채널_생성(MEMBER_SLACK_ID, FREE_CHAT.create(workspace));
        long freeChatId = 2L;
        구독_요청(token, freeChatId);

        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(noticeId, 1),
                new ChannelOrderRequest(freeChatId, 1)
        );

        // when
        ExtractableResponse<Response> response = 구독_채널_순서_변경_요청(token, request);

        // then
        상태코드_400_확인(response);
        에러코드_확인(response, "SUBSCRIPTION_DUPLICATE");
    }

    @Test
    void 구독_채널_순서_변경_시_해당_멤버가_구독한_적_없는_채널_ID가_포함된_경우_예외_발생() {
        // given
        채널_생성(MEMBER_SLACK_ID, NOTICE.create(workspace));
        long noticeId = 1L;

        채널_생성(MEMBER_SLACK_ID, FREE_CHAT.create(workspace));
        long freeChatId = 2L;
        구독_요청(token, freeChatId);

        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(noticeId, 1),
                new ChannelOrderRequest(freeChatId, 2)
        );

        // when
        ExtractableResponse<Response> response = 구독_채널_순서_변경_요청(token, request);

        // then
        상태코드_400_확인(response);
        에러코드_확인(response, "SUBSCRIPTION_NOT_EXIST");
    }

    @Test
    void 구독_채널_순서_변경_시_해당_멤버의_모든_구독_채널이_요청에_포함되지_않을_경우_예외_발생() {
        // given
        채널_생성(MEMBER_SLACK_ID, NOTICE.create(workspace));
        long noticeId = 1L;
        구독_요청(token, noticeId);

        채널_생성(MEMBER_SLACK_ID, FREE_CHAT.create(workspace));
        long freeChatId = 2L;
        구독_요청(token, freeChatId);

        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(freeChatId, 1)
        );

        // when
        ExtractableResponse<Response> response = 구독_채널_순서_변경_요청(token, request);

        // then
        상태코드_400_확인(response);
        에러코드_확인(response, "SUBSCRIPTION_NOT_EXIST");
    }

    @Test
    void 구독_중인_채널_다시_구독_요청() {
        // given
        채널_생성(MEMBER_SLACK_ID, FREE_CHAT.create(workspace));
        long freeChatId = 1L;
        구독_요청(token, freeChatId);

        // when
        ExtractableResponse<Response> response = 구독_요청(token, freeChatId);

        // then
        상태코드_400_확인(response);
        에러코드_확인(response, "SUBSCRIPTION_DUPLICATE");
    }

    @Test
    void 구독하지_않은_채널_구독_취소() {
        // given
        채널_생성(MEMBER_SLACK_ID, FREE_CHAT.create(workspace));
        long freeChatId = 1L;

        // when
        ExtractableResponse<Response> response = 구독_취소_요청(token, freeChatId);

        // then
        상태코드_400_확인(response);
        에러코드_확인(response, "SUBSCRIPTION_NOT_EXIST");
    }

    private List<Long> 구독한_채널_id_목록() {
        ExtractableResponse<Response> channelsResponse = 유저_전체_채널_목록_조회_요청(token);
        return channelsResponse.jsonPath()
                .getList("channels.", ChannelResponse.class)
                .stream()
                .filter(ChannelResponse::isSubscribed)
                .map(ChannelResponse::getId)
                .collect(Collectors.toList());
    }

    private void 구독이_올바른_순서로_조회됨(
            final ExtractableResponse<Response> response,
            final Long firstOrderedId,
            final Long secondOrderedId
    ) {
        List<ChannelSubscriptionResponse> channels = response.jsonPath()
                .getList("channels.", ChannelSubscriptionResponse.class);

        assertAll(() -> {
            assertThat(channels).hasSize(2);
            assertThat(channels.get(0).getId()).isEqualTo(firstOrderedId);
            assertThat(channels.get(0).getOrder()).isEqualTo(1);
            assertThat(channels.get(1).getId()).isEqualTo(secondOrderedId);
            assertThat(channels.get(1).getOrder()).isEqualTo(2);
        });
    }

    private ExtractableResponse<Response> 올바른_구독_채널_순서_변경_요청(final String token, final Long channelIdToSubscribe1,
                                                             final Long channelIdToSubscribe2) {
        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(channelIdToSubscribe2, 1),
                new ChannelOrderRequest(channelIdToSubscribe1, 2)
        );

        return 구독_채널_순서_변경_요청(token, request);
    }
}
