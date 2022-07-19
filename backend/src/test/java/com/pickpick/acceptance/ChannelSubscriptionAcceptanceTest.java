package com.pickpick.acceptance;

import com.pickpick.controller.dto.ChannelOrderRequest;
import com.pickpick.controller.dto.ChannelSubscriptionResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql({"/truncate.sql", "/channel.sql"})
@DisplayName("채널 구독 기능")
@SuppressWarnings("NonAsciiCharacters")
class ChannelSubscriptionAcceptanceTest extends ChannelAcceptanceTest {

    Long channelIdToSubscribe1;
    Long channelIdToSubscribe2;

    @BeforeEach
    void subscribe() {
        ExtractableResponse<Response> response = 유저_전체_채널_목록_조회_요청();
        List<Long> unsubscribedChannelIds = 구독중이_아닌_채널_id_목록_추출(response);

        channelIdToSubscribe1 = unsubscribedChannelIds.get(0);
        channelIdToSubscribe2 = unsubscribedChannelIds.get(1);

        구독_요청(channelIdToSubscribe1);
        구독_요청(channelIdToSubscribe2);
    }

    @Test
    void 채널_구독_조회() {
        ExtractableResponse<Response> response = 유저_구독_채널_목록_조회_요청();

        상태코드_200_확인(response);
        구독이_올바른_순서로_조회됨(response, channelIdToSubscribe1, channelIdToSubscribe2);
    }

    @Test
    void 구독_채널_순서_변경() {
        ExtractableResponse<Response> response = 구독_채널_순서_변경_요청();

        상태코드_200_확인(response);

        ExtractableResponse<Response> subscriptionResponse = 유저_구독_채널_목록_조회_요청();
        구독이_올바른_순서로_조회됨(subscriptionResponse, channelIdToSubscribe2, channelIdToSubscribe1);
    }


    private ExtractableResponse<Response> 유저_구독_채널_목록_조회_요청() {
        return getWithAuth("/api/channel-subscription", 2L);
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

    private ExtractableResponse<Response> 구독_채널_순서_변경_요청() {
        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(channelIdToSubscribe2, 1),
                new ChannelOrderRequest(channelIdToSubscribe1, 2)
        );

        return putWithAuth("/api/channel-subscription", request, 2L);
    }
}
