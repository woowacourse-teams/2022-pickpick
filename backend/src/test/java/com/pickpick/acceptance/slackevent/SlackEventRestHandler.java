package com.pickpick.acceptance.slackevent;

import static com.pickpick.acceptance.RestHandler.post;

import com.pickpick.channel.domain.Channel;
import com.pickpick.slackevent.application.SlackEvent;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("NonAsciiCharacters")
public class SlackEventRestHandler {

    private static final String SLACK_EVENT_API_URL = "/api/event";

    public static ExtractableResponse<Response> 회원가입(final String slackId) {
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

        return post(SLACK_EVENT_API_URL, request);
    }

    public static ExtractableResponse<Response> 멤버_정보_수정(final String slackId, final String realName,
                                                         final String displayName, final String thumbnailUrl) {
        Map<String, Object> request = updateEventRequest(slackId, realName, displayName, thumbnailUrl);
        return post(SLACK_EVENT_API_URL, request);
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

    private static Map<String, Object> updateEventRequest(final String slackId, final String realName,
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
}
