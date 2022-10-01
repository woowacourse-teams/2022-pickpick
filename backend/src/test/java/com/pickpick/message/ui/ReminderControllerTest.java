package com.pickpick.message.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pickpick.message.ui.dto.ReminderResponse;
import com.pickpick.message.ui.dto.ReminderResponses;
import com.pickpick.message.ui.dto.ReminderSaveRequest;
import com.pickpick.support.DocsControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class ReminderControllerTest extends DocsControllerTest {

    private static final String REMINDER_API_URL = "/api/reminders";

    @DisplayName("리마인더를 추가한다")
    @Test
    void save() throws Exception {
        String body = objectMapper.writeValueAsString(
                new ReminderSaveRequest(1L, LocalDateTime.now().plusDays(2))
        );
        ResultActions result = mockMvc.perform(post(REMINDER_API_URL, body))
                .andExpect(status().isCreated());

        // docs
        result.andDo(restDocs.document(
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
        when(reminderService.findOne(anyLong(), anyLong()))
                .thenReturn(createReminderResponse(1L, 2L, "메시지 내용", LocalDateTime.now()));

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.set("messageId", "2");

        ResultActions result = mockMvc.perform(getWithParams(REMINDER_API_URL, requestParams))
                .andExpect(status().isOk());

        // docs
        result.andDo(restDocs.document(
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
        when(reminderService.find(any(), anyLong()))
                .thenReturn(createReminderResponses());

        ResultActions result = mockMvc.perform(get(REMINDER_API_URL))
                .andExpect(status().isOk());

        // docs
        result.andDo(restDocs.document(
                requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                ),
                requestParameters(
                        parameterWithName("reminderId").optional().description("리마인더 아이디"),
                        parameterWithName("count").optional().description("조회할 리마인더 개수(기본: 20)")
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
                        fieldWithPath("hasFuture").type(JsonFieldType.BOOLEAN)
                                .description("더 미래의 리마인더 조회 가능 여부")
                )
        ));
    }

    @DisplayName("리마인더를 수정한다")
    @Test
    void update() throws Exception {
        String body = objectMapper.writeValueAsString(
                new ReminderSaveRequest(2L, LocalDateTime.now().plusDays(2))
        );

        ResultActions result = mockMvc.perform(put(REMINDER_API_URL, body))
                .andExpect(status().isOk());

        // docs
        result.andDo(restDocs.document(
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

        ResultActions result = mockMvc.perform(delete(REMINDER_API_URL, requestParams))
                .andExpect(status().isNoContent());

        // docs
        result.andDo(restDocs.document(
                requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                ),
                requestParameters(
                        parameterWithName("messageId").description("삭제 예정인 리마인더의 메세지 아이디")
                )
        ));
    }

    private ReminderResponses createReminderResponses() {
        return new ReminderResponses(
                List.of(
                        createReminderResponse(1L, 2L, "메시지 내용", LocalDateTime.now()),
                        createReminderResponse(2L, 3L, "다른 메시지 내용", LocalDateTime.now()),
                        createReminderResponse(3L, 25L, "또 다른 메시지 내용", LocalDateTime.now())
                ),
                true
        );
    }

    private ReminderResponse createReminderResponse(Long id, Long messageId, String text, LocalDateTime postedDate) {
        return ReminderResponse.builder()
                .id(id)
                .messageId(messageId)
                .username("써머")
                .userThumbnail("https://summer.png")
                .text(text)
                .postedDate(postedDate)
                .modifiedDate(postedDate)
                .remindDate(postedDate.plusDays(1))
                .build();
    }
}
