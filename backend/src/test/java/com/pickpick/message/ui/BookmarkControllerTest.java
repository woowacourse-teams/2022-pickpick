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
import com.pickpick.message.ui.dto.BookmarkRequest;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Sql({"/bookmark.sql"})
public class BookmarkControllerTest extends RestDocsTestSupport {

    private static final String BOOKMARK_API_URL = "/api/bookmarks";

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setup() {
        given(jwtTokenProvider.getPayload(any(String.class)))
                .willReturn("1");
    }

    @DisplayName("북마크를 조회한다")
    @Test
    void find() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BOOKMARK_API_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 1")
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
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
                                fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("마지막 메시지 여부")
                        )
                ));
    }

    @DisplayName("북마크를 추가한다")
    @Test
    void save() throws Exception {
        String body = objectMapper.writeValueAsString(
                new BookmarkRequest(1L)
        );
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BOOKMARK_API_URL)
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
                                fieldWithPath("messageId").type(JsonFieldType.NUMBER).description("북마크할 메세지 아이디")
                        )
                ));
    }

    @DisplayName("북마크를 삭제한다")
    @Test
    void delete() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.set("messageId", "2");
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(BOOKMARK_API_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 1")
                        .params(requestParams)
                )
                .andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 식별 토큰(Bearer)")
                        ),
                        requestParameters(
                                parameterWithName("messageId").description("북마크 삭제할 메세지 아이디")
                        )
                ));
    }
}
