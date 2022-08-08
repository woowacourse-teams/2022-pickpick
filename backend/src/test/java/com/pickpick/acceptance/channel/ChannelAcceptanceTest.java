package com.pickpick.acceptance.channel;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.channel.ui.dto.ChannelSubscriptionRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/truncate.sql", "/channel.sql"})
@DisplayName("채널 기능")
@SuppressWarnings("NonAsciiCharacters")
public class ChannelAcceptanceTest extends AcceptanceTest {

    protected static final String CHANNEL_SUBSCRIPTION_API_URL = "/api/channel-subscription";

    @Test
    void 유저_전체_채널_목록_조회() {
        // given & when
        ExtractableResponse<Response> response = 유저_전체_채널_목록_조회_요청();

        // then
        상태코드_200_확인(response);
        조회된_채널_목록_개수_확인(response, 6);
    }

    @Test
    void 채널_구독() {
        // given
        ExtractableResponse<Response> response = 유저_전체_채널_목록_조회_요청();
        List<Long> unsubscribedChannelIds = 구독중이_아닌_채널_id_목록_추출(response);
        Long channelIdToSubscribe = unsubscribedChannelIds.get(0);

        // when
        ExtractableResponse<Response> subscriptionResponse = 구독_요청(channelIdToSubscribe);

        // then
        상태코드_200_확인(subscriptionResponse);
        채널_구독_완료_확인(channelIdToSubscribe);
    }

    @Test
    void 채널_구독_취소() {
        // given
        ExtractableResponse<Response> response = 유저_전체_채널_목록_조회_요청();
        List<Long> unsubscribedChannelIds = 구독중이_아닌_채널_id_목록_추출(response);

        Long channelIdToSubscribe = unsubscribedChannelIds.get(0);
        Long channelIdToUnSubscribe = unsubscribedChannelIds.get(1);

        구독_요청(channelIdToSubscribe);
        구독_요청(channelIdToUnSubscribe);

        // when
        ExtractableResponse<Response> unsubscribeResponse = 구독_취소_요청(channelIdToUnSubscribe);

        // then
        상태코드_200_확인(unsubscribeResponse);
        채널_구독_취소_확인(channelIdToUnSubscribe);
    }

    protected ExtractableResponse<Response> 유저_전체_채널_목록_조회_요청() {
        return getWithCreateToken("/api/channels", 2L);
    }

    protected List<Long> 구독중이_아닌_채널_id_목록_추출(final ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getList("channels.", ChannelResponse.class)
                .stream()
                .filter(it -> !it.isSubscribed())
                .map(ChannelResponse::getId)
                .collect(Collectors.toList());
    }

    protected ExtractableResponse<Response> 구독_요청(final Long channelId) {
        ChannelSubscriptionRequest channelSubscriptionRequest = new ChannelSubscriptionRequest(channelId);
        return postWithCreateToken(CHANNEL_SUBSCRIPTION_API_URL, channelSubscriptionRequest, 2L);
    }

    protected ExtractableResponse<Response> 구독_취소_요청(final Long channelId) {
        return deleteWithCreateToken(CHANNEL_SUBSCRIPTION_API_URL + "?channelId=" + channelId, 2L);
    }

    private void 채널_구독_완료_확인(final Long channelIdToSubscribe) {
        ExtractableResponse<Response> response = 유저_전체_채널_목록_조회_요청();
        List<Long> unsubscribedChannelIds = 구독중이_아닌_채널_id_목록_추출(response);

        assertThat(unsubscribedChannelIds).doesNotContain(channelIdToSubscribe);
    }

    private void 채널_구독_취소_확인(final Long channelIdToUnSubscribe) {
        ExtractableResponse<Response> response = 유저_전체_채널_목록_조회_요청();
        List<Long> unsubscribedChannelIds = 구독중이_아닌_채널_id_목록_추출(response);

        assertThat(unsubscribedChannelIds).contains(channelIdToUnSubscribe);
    }

    private void 조회된_채널_목록_개수_확인(final ExtractableResponse<Response> response, final int expectedSize) {
        assertThat(response.jsonPath().getList("channels.", ChannelResponse.class)).hasSize(expectedSize);
    }
}
