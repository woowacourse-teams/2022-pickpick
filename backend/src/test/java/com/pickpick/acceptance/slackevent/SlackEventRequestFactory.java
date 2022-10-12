package com.pickpick.acceptance.slackevent;

import com.pickpick.fixture.ChannelFixture;
import com.pickpick.slackevent.application.SlackEvent;
import java.util.Map;
import java.util.UUID;

public class SlackEventRequestFactory {

    private SlackEventRequestFactory() {
    }

    public static Map<String, Object> urlVerifyEvent(final String token, final String type, final String challenge) {
        return Map.of("token", token, "type", type, "challenge", challenge);
    }

    public static Map<String, Object> memberJoinEvent(final String slackId, final String workspaceSlackId) {
        return memberEvent(SlackEvent.MEMBER_JOIN.getType(), slackId, workspaceSlackId, "realName", "displayName",
                "thumbnailUrl");
    }

    public static Map<String, Object> memberUpdateEvent(final String slackId, final String realName,
                                                        final String displayName, final String thumbnailUrl) {
        return memberEvent(SlackEvent.MEMBER_CHANGED.getType(), slackId, "", realName, displayName, thumbnailUrl);
    }

    private static Map<String, Object> memberEvent(final String subtype, final String slackId,
                                                   final String workspaceSlackId,
                                                   final String realName, final String displayName,
                                                   final String thumbnailUrl) {
        return Map.of(
                "team_id", workspaceSlackId,
                "event", Map.of(
                        "type", subtype,
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

    public static Map<String, Object> emptyMessageCreateEvent(final String memberSlackId, final String messageSlackId,
                                                              final String subtype, final String channelSlackId) {
        return messageCreateEvent(memberSlackId, messageSlackId, subtype, "1234567890.123456", "", channelSlackId);
    }

    public static Map<String, Object> messageCreateEvent(final String memberSlackId, final String messageSlackId,
                                                         final String subtype, final String channelSlackId) {
        return messageCreateEvent(memberSlackId, messageSlackId, subtype, "1234567890.123456", "text", channelSlackId);
    }

    public static Map<String, Object> messageCreateEvent(final String memberSlackId, final String messageSlackId,
                                                         final String subtype, final String timestamp,
                                                         final String text, final String channelSlackId) {
        String type = "event_callback";

        Map<String, Object> event = Map.of(
                "type", "message",
                "subtype", subtype,
                "channel", channelSlackId,
                "previous_message", Map.of("client_msg_id", messageSlackId),
                "message", Map.of(
                        "user", memberSlackId,
                        "ts", timestamp,
                        "text", text,
                        "client_msg_id", messageSlackId
                ),
                "client_msg_id", messageSlackId,
                "text", text,
                "user", memberSlackId,
                "ts", timestamp
        );

        return Map.of("type", type, "event", event);
    }

    public static Map<String, Object> threadBroadcastCreateEvent(final String memberSlackId) {
        String timestamp = "1234567890.123456";
        String text = "메시지 전송!";
        String slackMessageId = UUID.randomUUID().toString();

        String type = "event_callback";
        Map<String, Object> event = Map.of(
                "type", "message",
                "subtype", "message_changed",
                "channel", ChannelFixture.QNA.getSlackId(),
                "previous_message", Map.of("client_msg_id", slackMessageId),
                "message", Map.of(
                        "type", "message",
                        "subtype", "thread_broadcast",
                        "user", memberSlackId,
                        "ts", timestamp,
                        "text", text,
                        "client_msg_id", slackMessageId
                ),
                "client_msg_id", slackMessageId,
                "user", memberSlackId,
                "ts", timestamp
        );

        return Map.of("type", type, "event", event);
    }

    public static Map<String, Object> channelCreatedEvent(final String workspaceSlackId, final String channelSlackId,
                                                          final String channelName) {
        return channelEvent(workspaceSlackId, channelSlackId, channelName, SlackEvent.CHANNEL_CREATED);
    }

    public static Map<String, Object> channelRenameEvent(final String workspaceSlackId, final String channelSlackId,
                                                         final String channelName) {
        return channelEvent(workspaceSlackId, channelSlackId, channelName, SlackEvent.CHANNEL_RENAME);
    }

    private static Map<String, Object> channelEvent(final String workspaceSlackId, final String channelSlackId,
                                                    final String channelName, final SlackEvent slackEvent) {
        Map<String, Object> event = Map.of(
                "type", slackEvent.getType(),
                "subtype", "",
                "channel", Map.of(
                        "id", channelSlackId,
                        "name", channelName
                )
        );

        return Map.of(
                "type", "event_callback",
                "event", event,
                "team_id", workspaceSlackId
        );
    }
}
