package com.pickpick.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Sql({"/truncate.sql", "/message.sql"})
class MessageControllerTest extends RestDocsTestSupport {

    @DisplayName("메시지를 조회한다.")
    @Test
    void findAllMessageWithCondition() throws Exception {
        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.set("keyword", "A");
        requestParam.set("date", "2022-07-17T13:21:55");
        requestParam.set("channelIds", "5");
        requestParam.set("needPastMessage", "true");
        requestParam.set("messageId", "");
        requestParam.set("messageCount", "5");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/messages")
                        .params(requestParam)
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                                responseFields(
                                        fieldWithPath("messages.[].id").type(JsonFieldType.NUMBER).description("메시지 아이디"),
                                        fieldWithPath("messages.[].memberId").type(JsonFieldType.NUMBER).description("유저 아이디"),
                                        fieldWithPath("messages.[].username").type(JsonFieldType.STRING).description("유저 이름"),
                                        fieldWithPath("messages.[].userThumbnail").type(JsonFieldType.STRING).description("유저 프로필 사진"),
                                        fieldWithPath("messages.[].text").type(JsonFieldType.STRING).description("메시지 내용"),
                                        fieldWithPath("messages.[].postedDate").type(JsonFieldType.STRING).description("메시지 게시 날짜"),
                                        fieldWithPath("messages.[].modifiedDate").type(JsonFieldType.STRING).description("메시지 수정 날짜"),
                                        fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("마지막 메시지 여부")
                                )
                        )
                );
    }
}
