package com.pickpick.acceptance.slackevent;

import static com.pickpick.acceptance.RestHandler.post;

import com.pickpick.channel.domain.Channel;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("NonAsciiCharacters")
public class SlackEventRestHandler {

    private static final String SLACK_EVENT_API_URL = "/api/event";

    public static void 회원가입(final String slackId) {
        Map<String, Object> request = Map.of(
                "event", Map.of(
                        "type", "team_join",
                        "user", Map.of(
                                "id", slackId,
                                "profile", Map.of(
                                        "real_name", "봄",
                                        "display_name", "가을",
                                        "image_48", "bom.png"
                                )
                        )
                ));

        post(SLACK_EVENT_API_URL, request);
    }

    public static void 채널_생성(final String memberSlackId, final Channel channel) {
        post(SLACK_EVENT_API_URL, createEventRequest(memberSlackId, channel));
    }

    private static Map<String, Object> createEventRequest(final String memberSlackId, final Channel channel) {
        String timestamp = "1234567890.123456";
        String text = "메시지 전송!";
        String slackMessageId = UUID.randomUUID().toString();

        String type = "event_callback";
        Map<String, Object> event = Map.of(
                "type", "message",
                "subtype", "",
                "channel", channel.getSlackId(),
                "previous_message", Map.of("client_msg_id", slackMessageId),
                "message", Map.of(
                        "user", memberSlackId,
                        "ts", timestamp,
                        "text", text,
                        "client_msg_id", slackMessageId
                ),
                "client_msg_id", slackMessageId,
                "text", text,
                "user", memberSlackId,
                "ts", timestamp
        );

        return Map.of("type", type, "event", event);
    }
}
