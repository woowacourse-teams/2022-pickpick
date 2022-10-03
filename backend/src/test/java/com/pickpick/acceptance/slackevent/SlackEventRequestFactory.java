package com.pickpick.acceptance.slackevent;

import com.pickpick.channel.domain.Channel;
import com.pickpick.slackevent.application.SlackEvent;
import java.util.Map;
import java.util.UUID;

public class SlackEventRequestFactory {

    private SlackEventRequestFactory() {
    }

    public static Map<String, Object> memberJoinEvent(final String slackId) {
        return Map.of(
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
    }

    public static Map<String, Object> memberUpdateEvent(final String slackId, final String realName,
                                                        final String displayName, final String thumbnailUrl) {
        return Map.of("event", Map.of(
                        "type", SlackEvent.MEMBER_CHANGED.getType(),
                        "user", Map.of(
                                "id", slackId,
                                "profile", Map.of(
                                        "real_name", realName,
                                        "display_name", displayName,
                                        "image_48", thumbnailUrl
                                )
                        )
                )
        );
    }

    public static Map<String, Object> messageCreateEvent(final String subtype) {
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

    public static Map<String, Object> threadBroadcastCreateEvent() {
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

    public static Map<String, Object> channelCreateEvent(final String memberSlackId, final Channel channel) {
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
