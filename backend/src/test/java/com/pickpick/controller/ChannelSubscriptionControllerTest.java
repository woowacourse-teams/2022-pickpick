package com.pickpick.controller;

import com.pickpick.controller.dto.ChannelOrderRequest;
import com.pickpick.controller.dto.ChannelSubscriptionRequest;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/truncate.sql", "/channel.sql"})
class ChannelSubscriptionControllerTest extends RestDocsTestSupport {

    @DisplayName("멤버의 구독 중인 채널을 조회한다.")
    @Test
    void findAllSubscribedChannel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/channel-subscription")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 2"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
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
        String body = objectMapper.writeValueAsString(
                new ChannelSubscriptionRequest(1L)
        );
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/channel-subscription")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 2")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("채널 구독을 취소한다.")
    @Test
    void unsubscribeChannel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/channel-subscription/{channelId}", 2L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 2"))
                .andExpect(status().isOk());
    }

    @DisplayName("구독 채널의 순서를 변경한다.")
    @Test
    void updateOrderOfSubscribedChannel() throws Exception {
        String body = objectMapper.writeValueAsString(
                List.of(
                        new ChannelOrderRequest(5L, 1),
                        new ChannelOrderRequest(2L, 2)
                )
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/channel-subscription")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 2")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
