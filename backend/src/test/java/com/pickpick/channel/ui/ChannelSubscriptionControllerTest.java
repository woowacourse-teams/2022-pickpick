package com.pickpick.channel.ui;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.channel.ui.dto.ChannelOrderRequest;
import com.pickpick.channel.ui.dto.ChannelSubscriptionRequest;
import com.pickpick.config.RestDocsTestSupport;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Sql({"/channel.sql", "/channel-subscription.sql"})
class ChannelSubscriptionControllerTest extends RestDocsTestSupport {

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setup() {
        given(jwtTokenProvider.getPayload(any(String.class)))
                .willReturn("2");
    }

    @DisplayName("멤버의 구독 중인 채널을 조회한다.")
    @Test
    void findAllSubscribedChannel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/channel-subscription")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 2"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
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
        String body = objectMapper.writeValueAsString(
                new ChannelSubscriptionRequest(1L)
        );
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/channel-subscription")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 2")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
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
        mockMvc.perform(RestDocumentationRequestBuilders
                        .delete("/api/channel-subscription")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 2")
                        .param("channelId", "2")
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
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
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                        ),
                        requestFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("채널 아이디"),
                                fieldWithPath("[].order").type(JsonFieldType.NUMBER).description("구독 채널 순서")
                        )
                ));
    }
}
