package com.pickpick.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.slackevent.application.SlackEvent;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/truncate.sql","/member.sql"})
@DisplayName("멤버 기능")
@SuppressWarnings("NonAsciiCharacters")
class MemberEventAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/event";

    @Test
    void 멤버_수정_발생_시_프로필_이미지와_이름이_업데이트_된다() {
        // given
        Map<String, Object> memberUpdatedRequest = createEventRequest("tt","tt","test.png");

        // when
        ExtractableResponse<Response> memberChangedResponse = post(API_URL, memberUpdatedRequest);

        // then
        assertThat(memberChangedResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private Map<String, Object> createEventRequest(String realName, String displayName, String thumbnailUrl) {
        return Map.of(
                "type", SlackEvent.MEMBER_CHANGED.getType(),
                "user", Map.of(
                        "id", "U03MC231",
                        "profile", Map.of(
                                "real_name", realName,
                                "display_name", displayName,
                                "image_512", thumbnailUrl
                        )
                )
        );
    }
}
