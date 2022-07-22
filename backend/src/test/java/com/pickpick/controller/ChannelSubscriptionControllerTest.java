package com.pickpick.controller;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/truncate.sql", "/channel.sql"})
class ChannelSubscriptionControllerTest extends RestDocsTestSupport {

    @DisplayName("채널을 조회한다.")
    @Test
    void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/channels")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 2"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("channels.[].id").type(JsonFieldType.NUMBER).description("채널 아이디"),
                                fieldWithPath("channels.[].name").type(JsonFieldType.STRING).description("채널 이름"),
                                fieldWithPath("channels.[].isSubscribed").type(JsonFieldType.BOOLEAN).description("채널 구독 여부")
                        )
                ));
    }
}
