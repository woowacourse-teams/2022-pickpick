package com.pickpick.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메시지 기능")
@SuppressWarnings("NonAsciiCharacters")
class MessageAcceptanceTest extends AcceptanceTest {

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
        ExtractableResponse<Response> result = post("/api/message", request);

        // then
        assertThat(result.asString()).isEqualTo(challenge);
    }

    private void 메시지_저장_성공() {
        // given
        String user = "ABCD5N02W3N";
        String timestamp = "1234567890.123456";
        String text = "메시지 전송!";

        Map<String, String> event = Map.of("user", user, "ts", timestamp, "text", text);

        String type = "event_callback";
        Map<String, Object> request = Map.of("type",type,"event", event);

        // when
        ExtractableResponse<Response> result = post("/api/message", request);

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
