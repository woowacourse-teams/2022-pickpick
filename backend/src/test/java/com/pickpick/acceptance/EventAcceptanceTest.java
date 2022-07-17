package com.pickpick.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql("/message.sql")
@DisplayName("메시지 기능")
@SuppressWarnings("NonAsciiCharacters")
class EventAcceptanceTest extends AcceptanceTest {

    public static final String API_URL = "/api/event";

    @Test
    void 메시지_생성() {
        URI_검증_성공();
        메시지_저장_성공();
    }

    private void URI_검증_성공() {
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

    private void 메시지_저장_성공() {
        // given
        String user = "U03MC231";
        String timestamp = "1234567890.123456";
        String text = "메시지 전송!";
        String slackMessageId = "db8a1f84-8acf-46ab-b93d-85177cee3e97";

        Map<String, String> event = Map.of(
                "type", "message",
                "user", user,
                "ts", timestamp,
                "text", text,
                "client_msg_id", slackMessageId);

        String type = "event_callback";
        Map<String, Object> request = Map.of("type", type, "event", event);

        // when
        ExtractableResponse<Response> result = post(API_URL, request);

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
