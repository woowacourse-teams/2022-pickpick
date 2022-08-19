package com.pickpick.acceptance.slackevent;

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

    private static Map<String, Object> createEventRequest(final String subtype) {
        String user = "U03MC231";
        String timestamp = "1234567890.123456";
        String text = "메시지 전송!";
        String slackMessageId = "db8a1f84-8acf-46ab-b93d-85177cee3e97";

        String type = "event_callback";
        Map<String, Object> event = Map.of(
                "type", "message",
                "subtype", subtype,
                "channel", "ABC1234",
                "previous_message", Map.of("client_msg_id", slackMessageId),
                "message", Map.of(
                        "user", user,
                        "ts", timestamp,
                        "text", text,
                        "client_msg_id", slackMessageId
                ),
                "client_msg_id", slackMessageId,
                "text", text,
                "user", user,
                "ts", timestamp
        );

        return Map.of("type", type, "event", event);
    }

    private static Map<String, Object> createThreadBroadcastEventRequest() {
        String user = "U03MC231";
        String timestamp = "1234567890.123456";
        String text = "메시지 전송!";
        String slackMessageId = "db8a1f84-8acf-46ab-b93d-85177cee3e97";

        String type = "event_callback";
        Map<String, Object> event = Map.of(
                "type", "message",
                "subtype", "message_changed",
                "channel", "ABC1234",
                "previous_message", Map.of("client_msg_id", slackMessageId),
                "message", Map.of(
                        "type", "message",
                        "subtype", "thread_broadcast",
                        "user", user,
                        "ts", timestamp,
                        "text", text,
                        "client_msg_id", slackMessageId
                ),
                "client_msg_id", slackMessageId,
                "user", user,
                "ts", timestamp
        );

        return Map.of("type", type, "event", event);
    }

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
        Map<String, Object> messageCreatedRequest = createEventRequest("");

        // when
        ExtractableResponse<Response> response = post(MESSAGE_EVENT_API_URL, messageCreatedRequest);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 메시지_수정_요청_시_메시지_내용과_수정_시간이_업데이트_된다() {
        // given
        Map<String, Object> messageCreatedRequest = createEventRequest("");
        post(MESSAGE_EVENT_API_URL, messageCreatedRequest);

        Map<String, Object> messageChangedRequest = createEventRequest(SlackEvent.MESSAGE_CHANGED.getSubtype());

        // when
        ExtractableResponse<Response> response = post(MESSAGE_EVENT_API_URL, messageChangedRequest);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 메시지_삭제_요청_시_메시지가_삭제_된다() {
        // given
        Map<String, Object> messageCreatedRequest = createEventRequest("");
        post(MESSAGE_EVENT_API_URL, messageCreatedRequest);

        Map<String, Object> messageDeletedRequest = createEventRequest(SlackEvent.MESSAGE_DELETED.getSubtype());

        // when
        ExtractableResponse<Response> response = post(MESSAGE_EVENT_API_URL, messageDeletedRequest);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 스레드를_작성하면서_바로_채널로_전송_시_메시지가_저장된다() {
        // given
        Map<String, Object> messageThreadBroadcastRequest = createEventRequest("thread_broadcast");

        // when
        ExtractableResponse<Response> response = post(MESSAGE_EVENT_API_URL, messageThreadBroadcastRequest);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 스레드_작성_후_메뉴에서_채널로_전송_시_메시지가_저장된다() {
        // given
        Map<String, Object> messageThreadBroadcastRequest = createThreadBroadcastEventRequest();

        // when
        ExtractableResponse<Response> response = post(MESSAGE_EVENT_API_URL, messageThreadBroadcastRequest);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 파일_공유_메시지_요청_시_메시지가_저장된다() {
        // given
        Map<String, Object> messageCreatedRequest = createEventRequest("");
        post(MESSAGE_EVENT_API_URL, messageCreatedRequest);

        Map<String, Object> fileShareMessageRequest = createEventRequest(SlackEvent.MESSAGE_FILE_SHARE.getSubtype());

        // when
        ExtractableResponse<Response> response = post(MESSAGE_EVENT_API_URL, fileShareMessageRequest);

        // then
        상태코드_200_확인(response);
    }
}
