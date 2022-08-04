package com.pickpick.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pickpick.auth.support.JwtTokenProvider;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@Sql({"/truncate.sql", "/message.sql"})
class MessageControllerTest extends RestDocsTestSupport {

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setup() {
        given(jwtTokenProvider.getPayload(any(String.class)))
                .willReturn("1");
    }

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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 1")
                        .params(requestParam)
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                                ),
                                requestParameters(
                                        parameterWithName("keyword").optional().description("검색할 키워드"),
                                        parameterWithName("date").optional().description("검색 기준 날짜"),
                                        parameterWithName("channelIds").optional().description("검색할 채널 아이디(복수 가능)"),
                                        parameterWithName("needPastMessage").optional().description("불러올 메시지가 더 존재하는지"),
                                        parameterWithName("messageId").optional().description("메시지 아이디"),
                                        parameterWithName("messageCount").optional().description("한 번에 불러올 메시지 개수(default:20)")
                                ),
                                responseFields(
                                        fieldWithPath("messages.[].id").type(JsonFieldType.NUMBER)
                                                .description("메시지 아이디"),
                                        fieldWithPath("messages.[].memberId").type(JsonFieldType.NUMBER)
                                                .description("유저 아이디"),
                                        fieldWithPath("messages.[].username").type(JsonFieldType.STRING)
                                                .description("유저 이름"),
                                        fieldWithPath("messages.[].userThumbnail").type(JsonFieldType.STRING)
                                                .description("유저 프로필 사진"),
                                        fieldWithPath("messages.[].text").type(JsonFieldType.STRING)
                                                .description("메시지 내용"),
                                        fieldWithPath("messages.[].postedDate").type(JsonFieldType.STRING)
                                                .description("메시지 게시 날짜"),
                                        fieldWithPath("messages.[].modifiedDate").type(JsonFieldType.STRING)
                                                .description("메시지 수정 날짜"),
                                        fieldWithPath("messages.[].isBookmarked").type(JsonFieldType.BOOLEAN)
                                                .description("북마크 여부"),
                                        fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("마지막 메시지 여부"),
                                        fieldWithPath("isNeedPastMessage").type(JsonFieldType.BOOLEAN)
                                                .description("위/아래 스크롤 방향")
                                )
                        )
                );
    }
}
