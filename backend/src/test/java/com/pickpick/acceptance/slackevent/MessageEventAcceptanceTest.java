package com.pickpick.acceptance.slackevent;

import static com.pickpick.acceptance.RestHandler.post;
import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.slackevent.application.SlackEvent;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/message.sql"})
@DisplayName("메시지 이벤트 기능")
@SuppressWarnings("NonAsciiCharacters")
class MessageEventAcceptanceTest extends AcceptanceTest {

    private static final String MESSAGE_EVENT_API_URL = "/api/event";


    @Test
    void URL_검증_요청_시_challenge_를_응답한다() {
        // given
        String token = "token";
        String type = "url_verification";
        String challenge = "example123token123";

        Map<String, String> request = Map.of("token", token, "type", type, "challenge", challenge);

        // when
        ExtractableResponse<Response> response = post(MESSAGE_EVENT_API_URL, request);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 메시지_저장_성공() {
        // given
        Map<String, Object> messageCreatedRequest = SlackEventRequestFactory.messageCreateEvent("");

        // when
        ExtractableResponse<Response> response = post(MESSAGE_EVENT_API_URL, messageCreatedRequest);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 메시지_수정_요청_시_메시지_내용과_수정_시간이_업데이트_된다() {
        // given
        Map<String, Object> messageCreatedRequest = SlackEventRequestFactory.messageCreateEvent("");
        post(MESSAGE_EVENT_API_URL, messageCreatedRequest);

        Map<String, Object> messageChangedRequest = SlackEventRequestFactory.messageCreateEvent(
                SlackEvent.MESSAGE_CHANGED.getSubtype());

        // when
        ExtractableResponse<Response> response = post(MESSAGE_EVENT_API_URL, messageChangedRequest);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 메시지_삭제_요청_시_메시지가_삭제_된다() {
        // given
        Map<String, Object> messageCreatedRequest = SlackEventRequestFactory.messageCreateEvent("");
        post(MESSAGE_EVENT_API_URL, messageCreatedRequest);

        Map<String, Object> messageDeletedRequest = SlackEventRequestFactory.messageCreateEvent(
                SlackEvent.MESSAGE_DELETED.getSubtype());

        // when
        ExtractableResponse<Response> response = post(MESSAGE_EVENT_API_URL, messageDeletedRequest);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 스레드를_작성하면서_바로_채널로_전송_시_메시지가_저장된다() {
        // given
        Map<String, Object> messageThreadBroadcastRequest = SlackEventRequestFactory.messageCreateEvent(
                "thread_broadcast");

        // when
        ExtractableResponse<Response> response = post(MESSAGE_EVENT_API_URL, messageThreadBroadcastRequest);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 스레드_작성_후_메뉴에서_채널로_전송_시_메시지가_저장된다() {
        // given
        Map<String, Object> messageThreadBroadcastRequest = SlackEventRequestFactory.threadBroadcastCreateEvent();

        // when
        ExtractableResponse<Response> response = post(MESSAGE_EVENT_API_URL, messageThreadBroadcastRequest);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 파일_공유_메시지_요청_시_메시지가_저장된다() {
        // given
        Map<String, Object> messageCreatedRequest = SlackEventRequestFactory.messageCreateEvent("");
        post(MESSAGE_EVENT_API_URL, messageCreatedRequest);

        Map<String, Object> fileShareMessageRequest = SlackEventRequestFactory.messageCreateEvent(
                SlackEvent.MESSAGE_FILE_SHARE.getSubtype());

        // when
        ExtractableResponse<Response> response = post(MESSAGE_EVENT_API_URL, fileShareMessageRequest);

        // then
        상태코드_200_확인(response);
    }
}
