package com.pickpick.message.ui;

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
import com.pickpick.config.RestDocsTestSupport;
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

@Sql({"/truncate.sql", "/reminder.sql"})
public class ReminderControllerTest extends RestDocsTestSupport {

    private static final String REMINDER_API_URL = "/api/reminders";

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setup() {
        given(jwtTokenProvider.getPayload(any(String.class)))
                .willReturn("1");
    }

    @DisplayName("리마인더를 조회한다")
    @Test
    void find() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(REMINDER_API_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 1")
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                        ),
                        requestParameters(
                                parameterWithName("reminderId").optional().description("리마인더 아이디")
                        ),
                        responseFields(
                                fieldWithPath("reminders.[].id").type(JsonFieldType.NUMBER)
                                        .description("리마인더 아이디"),
                                fieldWithPath("reminders.[].messageId").type(JsonFieldType.NUMBER)
                                        .description("메시지 아이디"),
                                fieldWithPath("reminders.[].username").type(JsonFieldType.STRING)
                                        .description("유저 이름"),
                                fieldWithPath("reminders.[].userThumbnail").type(JsonFieldType.STRING)
                                        .description("유저 프로필 사진"),
                                fieldWithPath("reminders.[].text").type(JsonFieldType.STRING)
                                        .description("메시지 내용"),
                                fieldWithPath("reminders.[].postedDate").type(JsonFieldType.STRING)
                                        .description("메시지 게시 날짜"),
                                fieldWithPath("reminders.[].modifiedDate").type(JsonFieldType.STRING)
                                        .description("메시지 수정 날짜"),
                                fieldWithPath("reminders.[].remindDate").type(JsonFieldType.STRING)
                                        .description("리마인드 날짜"),
                                fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("마지막 리마인드 메시지 여부")
                        )
                ));
    }

    @DisplayName("리마인더를 삭제한다")
    @Test
    void delete() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.set("messageId", "2");
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(REMINDER_API_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 1")
                        .params(requestParams)
                )
                .andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                        ),
                        requestParameters(
                                parameterWithName("messageId").description("리마인더 삭제할 메세지 아이디")
                        )
                ));
    }
}
