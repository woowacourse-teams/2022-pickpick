package com.pickpick.acceptance.message;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.message.MessageRestHandler.메시지_조회;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.acceptance.message.MessageRestHandler.MessageRequestBuilder;
import com.pickpick.message.ui.dto.MessageResponse;
import com.pickpick.message.ui.dto.MessageResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/message.sql"})
@DisplayName("메시지 기능")
@SuppressWarnings("NonAsciiCharacters")
class MessageAcceptanceTest extends AcceptanceTest {

    private static final String MEMBER_ID = "1";

    @Test
    void 메시지_조회_시_needPastMessage_true_응답_확인() {
        // given
        String token = jwtTokenProvider.createToken(MEMBER_ID);
        MessageRequestBuilder request = new MessageRequestBuilder()
                .keyword("jupjup")
                .needPastMessage(true)
                .channelIds(5L)
                .build();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);
        MessageResponses messageResponses = response.as(MessageResponses.class);

        // then
        상태코드_200_확인(response);
        assertThat(messageResponses.isNeedPastMessage()).isTrue();
    }

    @Test
    void needPastMessage를_넘기지_않으면_true로_판단() {
        // given
        String token = jwtTokenProvider.createToken(MEMBER_ID);
        MessageRequestBuilder request = new MessageRequestBuilder()
                .keyword("jupjup")
                .channelIds(5L)
                .build();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);
        MessageResponses messageResponses = response.as(MessageResponses.class);

        // then
        상태코드_200_확인(response);
        assertThat(messageResponses.isNeedPastMessage()).isTrue();
    }

    @Test
    void 메시지_조회_시_needPastMessage가_False일_경우_응답_확인() {
        // given
        String token = jwtTokenProvider.createToken(MEMBER_ID);
        MessageRequestBuilder request = new MessageRequestBuilder()
                .keyword("jupjup")
                .needPastMessage(false)
                .channelIds(5L)
                .build();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);
        MessageResponses messageResponses = response.as(MessageResponses.class);

        // then
        상태코드_200_확인(response);
        assertThat(messageResponses.isNeedPastMessage()).isFalse();
    }

    @Test
    void 이미_리마인드_완료된_메시지_조회_시_isSetReminded가_false이고_remindDate가_null() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-13T00:00:00Z"));

        String token = jwtTokenProvider.createToken(MEMBER_ID);
        MessageRequestBuilder request = new MessageRequestBuilder()
                .channelIds(5L)
                .needPastMessage(true)
                .messageCount(1)
                .build();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);
        MessageResponse messageResponse = response.as(MessageResponses.class)
                .getMessages()
                .get(0);

        // then
        상태코드_200_확인(response);
        assertAll(
                () -> assertThat(messageResponse.isSetReminded()).isFalse(),
                () -> assertThat(messageResponse.getRemindDate()).isNull()
        );
    }

    @Test
    void 리마인드_해야하는_메시지_조회_시_isSetReminded가_true이고_remindDate에_값이_존재() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        String token = jwtTokenProvider.createToken(MEMBER_ID);
        MessageRequestBuilder request = new MessageRequestBuilder()
                .channelIds(5L)
                .needPastMessage(true)
                .messageCount(1)
                .build();

        // when
        ExtractableResponse<Response> response = 메시지_조회(token, request);
        MessageResponse messageResponse = response.as(MessageResponses.class)
                .getMessages()
                .get(0);

        // then
        상태코드_200_확인(response);
        assertAll(
                () -> assertThat(messageResponse.isSetReminded()).isTrue(),
                () -> assertThat(messageResponse.getRemindDate()).isNotNull()
        );
    }
}
