package com.pickpick.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/truncate.sql", "/message.sql"})
@DisplayName("메시지 기능")
@SuppressWarnings("NonAsciiCharacters")
class EventAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/event";
    private static final String MESSAGE_DELETED = "message_deleted";
    private static final String MESSAGE_CHANGED = "message_changed";

    @Test
    void URL_검증_요청_시_challenge_를_응답한다() {
        // given
        String token = "token";
        String type = "url_verification";
        String challenge = "example123token123";

        Map<String, String> request = Map.of("token", token, "type", type, "challenge", challenge);

        // when
        ExtractableResponse<Response> result = post(API_URL, request);

        // then
        assertThat(result.asString()).isEqualTo(challenge);
    }

    @Test
    void 메시지_저장_성공() {
        // given
        Map<String, Object> messageCreatedRequest = createEventRequest("");

        // when
        ExtractableResponse<Response> result = post(API_URL, messageCreatedRequest);

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 메시지_수정_요청_시_메시지_내용과_수정_시간이_업데이트_된다() {
        // given
        Map<String, Object> messageCreatedRequest = createEventRequest("");
        post(API_URL, messageCreatedRequest);

        Map<String, Object> messageChangedRequest = createEventRequest(MESSAGE_CHANGED);

        // when
        ExtractableResponse<Response> messageChangedResponse = post(API_URL, messageChangedRequest);

        // then
        assertThat(messageChangedResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 메시지_삭제_요청_시_메시지가_삭제_된다() {
        // given
        Map<String, Object> messageCreatedRequest = createEventRequest("");
        post(API_URL, messageCreatedRequest);

        Map<String, Object> messageDeletedRequest = createEventRequest(MESSAGE_DELETED);

        // when
        ExtractableResponse<Response> messageChangedResponse = post(API_URL, messageDeletedRequest);

        // then
        assertThat(messageChangedResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static Map<String, Object> createEventRequest(String subtype) {
        String user = "U03MC231";
        String timestamp = "1234567890.123456";
        String text = "메시지 전송!";
        String slackMessageId = "db8a1f84-8acf-46ab-b93d-85177cee3e97";

        String type = "event_callback";
        Map<String, Object> event = Map.of(
                "type", "message",
                "subtype", subtype,
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
}
