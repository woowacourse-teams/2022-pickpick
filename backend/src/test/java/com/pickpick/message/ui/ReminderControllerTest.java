package com.pickpick.message.ui;

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
import com.pickpick.config.RestDocsTestSupport;
import com.pickpick.message.ui.dto.ReminderSaveRequest;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Sql({"/reminder.sql"})
public class ReminderControllerTest extends RestDocsTestSupport {

    private static final String REMINDER_API_URL = "/api/reminders";

    @SpyBean
    private Clock clock;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setup() {
        given(jwtTokenProvider.getPayload(any(String.class)))
                .willReturn("1");
    }

    @DisplayName("리마인더를 추가한다")
    @Test
    void save() throws Exception {
        String body = objectMapper.writeValueAsString(
                new ReminderSaveRequest(1L, LocalDateTime.now().plusDays(2))
        );
        mockMvc.perform(MockMvcRequestBuilders
                        .post(REMINDER_API_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 1")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                        ),
                        requestFields(
                                fieldWithPath("messageId").type(JsonFieldType.NUMBER).description("리마인드할 메세지 아이디"),
                                fieldWithPath("reminderDate").type(JsonFieldType.STRING).description("리마인드할 날짜")
                        )
                ));
    }

    @DisplayName("리마인더를 단건 조회한다")
    @Test
    void findOne() throws Exception {
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.set("messageId", "2");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(REMINDER_API_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 1")
                        .params(requestParams)
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                        ),
                        requestParameters(
                                parameterWithName("messageId").optional().description("메시지 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("리마인더 아이디"),
                                fieldWithPath("messageId").type(JsonFieldType.NUMBER)
                                        .description("메시지 아이디"),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                        .description("유저 이름"),
                                fieldWithPath("userThumbnail").type(JsonFieldType.STRING)
                                        .description("유저 프로필 사진"),
                                fieldWithPath("text").type(JsonFieldType.STRING)
                                        .description("메시지 내용"),
                                fieldWithPath("postedDate").type(JsonFieldType.STRING)
                                        .description("메시지 게시 날짜"),
                                fieldWithPath("modifiedDate").type(JsonFieldType.STRING)
                                        .description("메시지 수정 날짜"),
                                fieldWithPath("remindDate").type(JsonFieldType.STRING)
                                        .description("리마인드 날짜")
                        )
                ));
    }

    @DisplayName("리마인더 목록을 조회한다")
    @Test
    void find() throws Exception {
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

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
                                fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("마지막 리마인더 메시지 여부")
                        )
                ));
    }

    @DisplayName("리마인더를 수정한다")
    @Test
    void update() throws Exception {
        String body = objectMapper.writeValueAsString(
                new ReminderSaveRequest(2L, LocalDateTime.now().plusDays(2))
        );
        mockMvc.perform(MockMvcRequestBuilders
                        .put(REMINDER_API_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 1")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                        ),
                        requestFields(
                                fieldWithPath("messageId").type(JsonFieldType.NUMBER)
                                        .description("수정 예정인 리마인더의 메세지 아이디"),
                                fieldWithPath("reminderDate").type(JsonFieldType.STRING).description("수정할 날짜")
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
                                parameterWithName("messageId").description("삭제 예정인 리마인더의 메세지 아이디")
                        )
                ));
    }
}
