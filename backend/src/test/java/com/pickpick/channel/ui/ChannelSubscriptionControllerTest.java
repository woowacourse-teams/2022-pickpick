package com.pickpick.channel.ui;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pickpick.channel.ui.dto.ChannelOrderRequest;
import com.pickpick.channel.ui.dto.ChannelSubscriptionRequest;
import com.pickpick.channel.ui.dto.ChannelSubscriptionResponse;
import com.pickpick.channel.ui.dto.ChannelSubscriptionResponses;
import com.pickpick.support.DocsControllerTestBase;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class ChannelSubscriptionControllerTest extends DocsControllerTestBase {

    private static final String API_CHANNEL_SUBSCRIPTION = "/api/channel-subscription";

    @DisplayName("멤버의 구독 중인 채널을 조회한다.")
    @Test
    void findAllSubscribedChannel() throws Exception {
        ChannelSubscriptionResponses responses = createChannelSubscriptionResponses();

        when(channelSubscriptionService.findByMemberId(any()))
                .thenReturn(responses);

        ResultActions result = mockMvc.perform(get(API_CHANNEL_SUBSCRIPTION))
                .andExpect(status().isOk());

        // docs
        result.andDo(restDocs.document(
                requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                ),
                responseFields(
                        fieldWithPath("channels.[].id").type(JsonFieldType.NUMBER).description("채널 아이디"),
                        fieldWithPath("channels.[].name").type(JsonFieldType.STRING).description("채널 이름"),
                        fieldWithPath("channels.[].order").type(JsonFieldType.NUMBER).description("채널 구독 순서")
                )
        ));
    }

    @DisplayName("채널을 구독한다.")
    @Test
    void subscribeChannel() throws Exception {
        ChannelSubscriptionRequest subscriptionRequest = new ChannelSubscriptionRequest(1L);
        doNothing().when(channelSubscriptionService)
                .save(any(), anyLong());

        String body = objectMapper.writeValueAsString(subscriptionRequest);

        ResultActions result = mockMvc.perform(post(API_CHANNEL_SUBSCRIPTION, body))
                .andExpect(status().isOk());

        // docs
        result.andDo(restDocs.document(
                requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                ),
                requestFields(
                        fieldWithPath("channelId").type(JsonFieldType.NUMBER).description("구독할 채널 아이디")
                )
        ));
    }

    @DisplayName("채널 구독을 취소한다.")
    @Test
    void unsubscribeChannel() throws Exception {
        doNothing().when(channelSubscriptionService)
                .delete(anyLong(), anyLong());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("channelId", "2");

        ResultActions result = mockMvc.perform(delete(API_CHANNEL_SUBSCRIPTION, params))
                .andExpect(status().isOk());

        // docs
        result.andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                        ),
                        requestParameters(
                                parameterWithName("channelId").description("채널 아이디")
                        )
                )
        );
    }

    @DisplayName("구독 채널의 순서를 변경한다.")
    @Test
    void updateOrderOfSubscribedChannel() throws Exception {
        doNothing().when(channelSubscriptionService)
                .updateOrders(any(), anyLong());

        String body = objectMapper.writeValueAsString(
                List.of(
                        new ChannelOrderRequest(5L, 1),
                        new ChannelOrderRequest(2L, 2)
                )
        );

        ResultActions result = mockMvc.perform(put(API_CHANNEL_SUBSCRIPTION, body))
                .andExpect(status().isOk());

        // docs
        result.andDo(restDocs.document(
                requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                ),
                requestFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("채널 아이디"),
                        fieldWithPath("[].order").type(JsonFieldType.NUMBER).description("구독 채널 순서")
                )
        ));
    }

    private ChannelSubscriptionResponses createChannelSubscriptionResponses() {
        return new ChannelSubscriptionResponses(
                List.of(
                        ChannelSubscriptionResponse.builder()
                                .id(3L)
                                .name("공지사항")
                                .order(1)
                                .build(),
                        ChannelSubscriptionResponse.builder()
                                .id(2L)
                                .name("잡담")
                                .order(2)
                                .build(),
                        ChannelSubscriptionResponse.builder()
                                .id(1L)
                                .name("질문")
                                .order(3)
                                .build()
                )
        );
    }
}
