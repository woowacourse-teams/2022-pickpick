package com.pickpick.acceptance.message;

import static com.pickpick.acceptance.RestHandler.getWithToken;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class MessageRestHandler {

    private static final String MESSAGE_API_URL = "/api/messages";

    public static ExtractableResponse<Response> 메시지_조회(final String token, final MessageRequestBuilder request) {
        return getWithToken(MESSAGE_API_URL, token, request.getParams());
    }

    public static class MessageRequestBuilder {

        private final Map<String, Object> params = new HashMap<>();

        public MessageRequestBuilder keyword(final String keyword) {
            params.put("keyword", keyword);
            return this;
        }

        public MessageRequestBuilder date(final LocalDateTime date) {
            params.put("date", date);
            return this;
        }

        public MessageRequestBuilder channelIds(final long... channelIds) {
            String string = Arrays.stream(channelIds)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(","));

            params.put("channelIds", string);
            return this;
        }

        public MessageRequestBuilder needPastMessage(final boolean needPastMessage) {
            params.put("needPastMessage", needPastMessage);
            return this;
        }

        public MessageRequestBuilder messageId(final long messageId) {
            params.put("messageId", messageId);
            return this;
        }

        public MessageRequestBuilder messageCount(final int messageCount) {
            params.put("messageCount", messageCount);
            return this;
        }

        public MessageRequestBuilder build() {
            return this;
        }

        public Map<String, Object> getParams() {
            return params;
        }
    }
}
