package com.pickpick.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pickpick.auth.support.JwtTokenProvider;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/truncate.sql", "/channel.sql", "/channel-subscription.sql"})
class ChannelControllerTest extends RestDocsTestSupport {

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setup() {
        given(jwtTokenProvider.getPayload(any(String.class)))
                .willReturn("2");
    }

    @DisplayName("구독 여부를 포함하여 채널을 조회한다.")
    @Test
    void findAll() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/api/channels")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 2"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                        ),
                        responseFields(
                                fieldWithPath("channels.[].id").type(JsonFieldType.NUMBER).description("채널 아이디"),
                                fieldWithPath("channels.[].name").type(JsonFieldType.STRING).description("채널 이름"),
                                fieldWithPath("channels.[].isSubscribed").type(JsonFieldType.BOOLEAN)
                                        .description("채널 구독 여부")
                        )
                ));
    }
}
