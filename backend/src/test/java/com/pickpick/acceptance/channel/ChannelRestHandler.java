package com.pickpick.acceptance.channel;

import static com.pickpick.acceptance.RestHandler.deleteWithToken;
import static com.pickpick.acceptance.RestHandler.getWithToken;
import static com.pickpick.acceptance.RestHandler.postWithToken;
import static com.pickpick.acceptance.RestHandler.putWithToken;

import com.pickpick.channel.ui.dto.ChannelOrderRequest;
import com.pickpick.channel.ui.dto.ChannelSubscriptionRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class ChannelRestHandler {

    private static final String CHANNEL_API_URL = "/api/channels";
    private static final String CHANNEL_SUBSCRIPTION_API_URL = "/api/channel-subscription";

    public static ExtractableResponse<Response> 유저_전체_채널_목록_조회_요청(final String token) {
        return getWithToken(CHANNEL_API_URL, token);
    }

    public static ExtractableResponse<Response> 채널_구독_요청(final String token, final Long channelId) {
        ChannelSubscriptionRequest channelSubscriptionRequest = new ChannelSubscriptionRequest(channelId);
        return postWithToken(CHANNEL_SUBSCRIPTION_API_URL, channelSubscriptionRequest, token);
    }

    public static ExtractableResponse<Response> 유저가_구독한_채널_목록_조회_요청(final String token) {
        return getWithToken(CHANNEL_SUBSCRIPTION_API_URL, token);
    }

    public static ExtractableResponse<Response> 구독한_채널_순서_변경_요청(final String token,
                                                                final List<ChannelOrderRequest> request) {
        return putWithToken(CHANNEL_SUBSCRIPTION_API_URL, request, token);
    }

    public static ExtractableResponse<Response> 채널_구독_취소_요청(final String token, final long channelId) {
        return deleteWithToken(CHANNEL_SUBSCRIPTION_API_URL, token, Map.of("channelId", channelId));
    }
}
