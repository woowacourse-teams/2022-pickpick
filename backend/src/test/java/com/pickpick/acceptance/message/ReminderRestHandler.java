package com.pickpick.acceptance.message;

import static com.pickpick.acceptance.RestHandler.deleteWithToken;
import static com.pickpick.acceptance.RestHandler.getWithToken;
import static com.pickpick.acceptance.RestHandler.postWithToken;
import static com.pickpick.acceptance.RestHandler.putWithToken;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class ReminderRestHandler {

    private static final String REMINDER_API_URL = "/api/reminders";

    public static ExtractableResponse<Response> 리마인더_생성(final String token, final long messageId,
                                                        final LocalDateTime reminderDate) {
        Map<String, Object> request = Map.of("messageId", messageId, "reminderDate", reminderDate);
        return postWithToken(REMINDER_API_URL, request, token);
    }

    public static ExtractableResponse<Response> 리마인더_단건_조회(final String token, final long messageId) {
        return getWithToken(REMINDER_API_URL, token, Map.of("messageId", messageId));
    }

    public static ExtractableResponse<Response> 리마인더_목록_조회(final String token, final Long reminderId,
                                                           final Integer count) {
        Map<String, Object> request = new HashMap<>();
        if (reminderId != null) {
            request.put("reminderId", reminderId);
        }
        if (count != null) {
            request.put("count", count);
        }
        return getWithToken(REMINDER_API_URL, token, request);
    }

    public static ExtractableResponse<Response> 리마인더_수정(final String token, final long messageId,
                                                        final LocalDateTime reminderDate) {
        Map<String, Object> request = Map.of("messageId", messageId, "reminderDate", reminderDate);
        return putWithToken(REMINDER_API_URL, request, token);
    }

    public static ExtractableResponse<Response> 리마인더_삭제(final String token, final long messageId) {
        return deleteWithToken(REMINDER_API_URL, token, Map.of("messageId", messageId));
    }
}
