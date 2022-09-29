package com.pickpick.channel.ui;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.channel.ui.dto.ChannelResponses;
import com.pickpick.config.RestDocsTestSupport;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class ChannelControllerTest extends RestDocsTestSupport {

    @DisplayName("구독 여부를 포함하여 채널을 조회한다.")
    @Test
    void findAll() throws Exception {
        ChannelResponses channels = createChannelResponses();
        when(channelService.findAll(anyLong()))
                .thenReturn(channels);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/api/channels")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer provided.jwt.token"))
                .andExpect(status().isOk());

        //docs
        result.andDo(restDocs.document(
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

    private ChannelResponses createChannelResponses() {
        return new ChannelResponses(
                List.of(
                        ChannelResponse.builder()
                                .id(1L)
                                .name("공지사항")
                                .subscribed(false)
                                .build(),
                        ChannelResponse.builder()
                                .id(2L)
                                .name("잡담")
                                .subscribed(true)
                                .build()
                )
        );
    }
}
