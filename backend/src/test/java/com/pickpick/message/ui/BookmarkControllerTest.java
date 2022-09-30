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

import com.pickpick.config.RestDocsTestSupport;
import com.pickpick.message.ui.dto.BookmarkRequest;
import com.pickpick.message.ui.dto.BookmarkResponse;
import com.pickpick.message.ui.dto.BookmarkResponses;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class BookmarkControllerTest extends RestDocsTestSupport {

    private static final String BOOKMARK_API_URL = "/api/bookmarks";

    @DisplayName("북마크를 조회한다")
    @Test
    void find() throws Exception {
        BookmarkResponses responses = createBookmarkResponses();

        when(bookmarkService.find(any(), anyLong()))
                .thenReturn(responses);

        ResultActions result = mockMvc.perform(getRequest(BOOKMARK_API_URL))
                .andExpect(status().isOk());

        // docs
        result.andDo(restDocs.document(
                requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                ),
                responseFields(
                        fieldWithPath("bookmarks.[].id").type(JsonFieldType.NUMBER)
                                .description("북마크 아이디"),
                        fieldWithPath("bookmarks.[].messageId").type(JsonFieldType.NUMBER)
                                .description("메시지 아이디"),
                        fieldWithPath("bookmarks.[].memberId").type(JsonFieldType.NUMBER)
                                .description("유저 아이디"),
                        fieldWithPath("bookmarks.[].username").type(JsonFieldType.STRING)
                                .description("유저 이름"),
                        fieldWithPath("bookmarks.[].userThumbnail").type(JsonFieldType.STRING)
                                .description("유저 프로필 사진"),
                        fieldWithPath("bookmarks.[].text").type(JsonFieldType.STRING)
                                .description("메시지 내용"),
                        fieldWithPath("bookmarks.[].postedDate").type(JsonFieldType.STRING)
                                .description("메시지 게시 날짜"),
                        fieldWithPath("bookmarks.[].modifiedDate").type(JsonFieldType.STRING)
                                .description("메시지 수정 날짜"),
                        fieldWithPath("hasPast").type(JsonFieldType.BOOLEAN).description("더 과거의 북마크 조회 가능 여부")
                )
        ));
    }

    @DisplayName("북마크를 추가한다")
    @Test
    void save() throws Exception {
        String body = objectMapper.writeValueAsString(
                new BookmarkRequest(1L)
        );
        ResultActions result = mockMvc.perform(postRequest(BOOKMARK_API_URL, body))
                .andExpect(status().isCreated());

        // docs
        result.andDo(restDocs.document(
                requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                ),
                requestFields(
                        fieldWithPath("messageId").type(JsonFieldType.NUMBER).description("북마크할 메세지 아이디")
                )
        ));
    }

    @DisplayName("북마크를 삭제한다")
    @Test
    void delete() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.set("messageId", "2");

        ResultActions result = mockMvc.perform(deleteRequest(BOOKMARK_API_URL, requestParams))
                .andExpect(status().isNoContent());

        // docs
        result.andDo(restDocs.document(
                requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                ),
                requestParameters(
                        parameterWithName("messageId").description("북마크 삭제할 메세지 아이디")
                )
        ));
    }

    private BookmarkResponses createBookmarkResponses() {
        return new BookmarkResponses(
                List.of(
                        BookmarkResponse.builder()
                                .id(1L)
                                .messageId(22L)
                                .memberId(1L)
                                .username("써머")
                                .userThumbnail("https://summer.png")
                                .text("메시지 내용")
                                .postedDate(LocalDateTime.now().minusDays(1))
                                .modifiedDate(LocalDateTime.now().minusDays(1))
                                .build(),
                        BookmarkResponse.builder()
                                .id(2L)
                                .messageId(24L)
                                .memberId(1L)
                                .username("써머")
                                .userThumbnail("https://summer.png")
                                .text("또 다른 메시지 내용")
                                .postedDate(LocalDateTime.now())
                                .modifiedDate(LocalDateTime.now())
                                .build(),
                        BookmarkResponse.builder()
                                .id(3L)
                                .messageId(33L)
                                .memberId(3L)
                                .username("연로그")
                                .userThumbnail("https://yeonlog.png")
                                .text("다른 메시지 내용")
                                .postedDate(LocalDateTime.now().minusDays(2))
                                .modifiedDate(LocalDateTime.now().minusDays(2))
                                .build()
                ),
                true
        );
    }
}
