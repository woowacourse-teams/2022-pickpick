package com.pickpick.acceptance;

import com.pickpick.slackevent.application.SlackEvent;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/truncate.sql", "/member.sql"})
@DisplayName("멤버 기능")
@SuppressWarnings("NonAsciiCharacters")
class MemberEventAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/event";

    @Test
    void 멤버_수정_발생_시_프로필_이미지와_이름이_업데이트_된다() {
        // given
        Map<String, Object> memberUpdatedRequest = createEventRequest("실제이름", "표시이름", "test.png");

        // when
        ExtractableResponse<Response> memberChangedResponse = post(API_URL, memberUpdatedRequest);

        // then
        상태코드_200_확인(memberChangedResponse);
    }

    private Map<String, Object> createEventRequest(final String realName, final String displayName,
                                                   final String thumbnailUrl) {
        return Map.of("event", Map.of(
                        "type", SlackEvent.MEMBER_CHANGED.getType(),
                        "user", Map.of(
                                "id", "U03MC231",
                                "profile", Map.of(
                                        "real_name", realName,
                                        "display_name", displayName,
                                        "image_512", thumbnailUrl
                                )
                        )
                )
        );
    }
}
