package com.pickpick.acceptance.channel;

import static com.pickpick.acceptance.RestHandler.deleteWithToken;
import static com.pickpick.acceptance.RestHandler.getWithToken;
import static com.pickpick.acceptance.RestHandler.postWithToken;
import static com.pickpick.acceptance.RestHandler.putWithToken;
import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_400_확인;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.channel.ui.dto.ChannelOrderRequest;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.channel.ui.dto.ChannelSubscriptionRequest;
import com.pickpick.channel.ui.dto.ChannelSubscriptionResponse;
import com.pickpick.config.dto.ErrorResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/channel.sql"})
@DisplayName("채널 구독 기능")
@SuppressWarnings("NonAsciiCharacters")
class ChannelSubscriptionAcceptanceTest extends ChannelAcceptanceTest {

    private static final String CHANNEL_SUBSCRIPTION_API_URL = "/api/channel-subscription";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Long channelIdToSubscribe1;
    private Long channelIdToSubscribe2;
    private String token;

    @BeforeEach
    void subscribe() {
        token = jwtTokenProvider.createToken("2");

        ExtractableResponse<Response> response = 유저_전체_채널_목록_조회_요청();
        List<Long> unsubscribedChannelIds = 구독중이_아닌_채널_id_목록_추출(response);

        channelIdToSubscribe1 = unsubscribedChannelIds.get(0);
        channelIdToSubscribe2 = unsubscribedChannelIds.get(1);

        구독_요청(channelIdToSubscribe1);
        구독_요청(channelIdToSubscribe2);
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

    private void 채널_구독_완료_확인(final Long channelIdToSubscribe) {
        ExtractableResponse<Response> response = 유저_전체_채널_목록_조회_요청();
        List<Long> unsubscribedChannelIds = 구독중이_아닌_채널_id_목록_추출(response);

        assertThat(unsubscribedChannelIds).isNotEmpty();
        assertThat(unsubscribedChannelIds).doesNotContain(channelIdToSubscribe);
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

    private void 채널_구독_취소_확인(final Long channelIdToUnSubscribe) {
        ExtractableResponse<Response> response = 유저_전체_채널_목록_조회_요청();
        List<Long> unsubscribedChannelIds = 구독중이_아닌_채널_id_목록_추출(response);

        assertThat(unsubscribedChannelIds).contains(channelIdToUnSubscribe);
    }

    @Test
    void 채널_구독_조회() {
        // given & when
        ExtractableResponse<Response> response = 유저_구독_채널_목록_조회_요청();

        // then
        상태코드_200_확인(response);
        구독이_올바른_순서로_조회됨(response, channelIdToSubscribe1, channelIdToSubscribe2);
    }

    @Test
    void 구독_채널_순서_변경() {
        // given & when
        ExtractableResponse<Response> response = 올바른_구독_채널_순서_변경_요청();

        // then
        상태코드_200_확인(response);

        ExtractableResponse<Response> subscriptionResponse = 유저_구독_채널_목록_조회_요청();
        구독이_올바른_순서로_조회됨(subscriptionResponse, channelIdToSubscribe2, channelIdToSubscribe1);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 구독_채널_순서_변경_시_1보다_작은_순서가_들어올_경우_예외_발생(int invalidViewOrder) {
        // given
        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(channelIdToSubscribe1, invalidViewOrder),
                new ChannelOrderRequest(channelIdToSubscribe2, 1)
        );

        // when
        ExtractableResponse<Response> response = 구독_채널_순서_변경_요청(request);

        // then
        상태코드_400_확인(response);
        assertThat(response.jsonPath().getObject("", ErrorResponse.class).getCode()).isEqualTo(
                "SUBSCRIPTION_INVALID_ORDER");
    }

    @Test
    void 구독_채널_순서_변경_시_중복된_순서가_들어올_경우_예외_발생() {
        // given
        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(channelIdToSubscribe1, 1),
                new ChannelOrderRequest(channelIdToSubscribe2, 1)
        );

        // when
        ExtractableResponse<Response> response = 구독_채널_순서_변경_요청(request);

        // then
        상태코드_400_확인(response);
        assertThat(response.jsonPath().getObject("", ErrorResponse.class).getCode()).isEqualTo(
                "SUBSCRIPTION_DUPLICATE");
    }

    @Test
    void 구독_채널_순서_변경_시_해당_멤버가_구독한_적_없는_채널_ID가_포함된_경우_예외_발생() {
        // given
        구독_취소_요청(channelIdToSubscribe1);

        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(channelIdToSubscribe1, 1),
                new ChannelOrderRequest(channelIdToSubscribe2, 2)
        );

        // when
        ExtractableResponse<Response> response = 구독_채널_순서_변경_요청(request);

        // then
        상태코드_400_확인(response);
        assertThat(response.jsonPath().getObject("", ErrorResponse.class).getCode()).isEqualTo(
                "SUBSCRIPTION_NOT_EXIST");
    }

    @Test
    void 구독_채널_순서_변경_시_해당_멤버의_모든_구독_채널이_요청에_포함되지_않을_경우_예외_발생() {
        // given
        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(channelIdToSubscribe1, 1)
        );

        // when
        ExtractableResponse<Response> response = 구독_채널_순서_변경_요청(request);

        // then
        상태코드_400_확인(response);
        assertThat(response.jsonPath().getObject("", ErrorResponse.class).getCode()).isEqualTo(
                "SUBSCRIPTION_NOT_EXIST");
    }

    @Test
    void 구독_중인_채널_다시_구독_요청() {
        // given & when
        ExtractableResponse<Response> response = 구독_요청(channelIdToSubscribe1);

        // then
        상태코드_400_확인(response);
        assertThat(response.jsonPath().getObject("", ErrorResponse.class).getCode()).isEqualTo(
                "SUBSCRIPTION_DUPLICATE");
    }

    @Test
    void 구독하지_않은_채널_구독_취소() {
        // given
        구독_취소_요청(channelIdToSubscribe1);

        // when
        ExtractableResponse<Response> response = 구독_취소_요청(channelIdToSubscribe1);

        // then
        상태코드_400_확인(response);
        assertThat(response.jsonPath().getObject("", ErrorResponse.class).getCode()).isEqualTo(
                "SUBSCRIPTION_NOT_EXIST");
    }


    private ExtractableResponse<Response> 유저_구독_채널_목록_조회_요청() {
        return getWithToken(CHANNEL_SUBSCRIPTION_API_URL, token);
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

    private ExtractableResponse<Response> 구독_채널_순서_변경_요청(final List<ChannelOrderRequest> request) {
        return putWithToken(CHANNEL_SUBSCRIPTION_API_URL, request, token);
    }

    private ExtractableResponse<Response> 올바른_구독_채널_순서_변경_요청() {
        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(channelIdToSubscribe2, 1),
                new ChannelOrderRequest(channelIdToSubscribe1, 2)
        );

        return 구독_채널_순서_변경_요청(request);
    }

    private List<Long> 구독중이_아닌_채널_id_목록_추출(final ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getList("channels.", ChannelResponse.class)
                .stream()
                .filter(it -> !it.isSubscribed())
                .map(ChannelResponse::getId)
                .collect(Collectors.toList());
    }

    private ExtractableResponse<Response> 구독_요청(final Long channelId) {
        ChannelSubscriptionRequest channelSubscriptionRequest = new ChannelSubscriptionRequest(channelId);
        return postWithToken(CHANNEL_SUBSCRIPTION_API_URL, channelSubscriptionRequest, token);
    }

    private ExtractableResponse<Response> 구독_취소_요청(final Long channelId) {
        return deleteWithToken(CHANNEL_SUBSCRIPTION_API_URL + "?channelId=" + channelId, token);
    }
}
