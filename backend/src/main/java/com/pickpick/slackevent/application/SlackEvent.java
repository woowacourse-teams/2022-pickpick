package com.pickpick.slackevent.application;

import com.pickpick.exception.SlackEventNotFoundException;
import java.util.Arrays;
import java.util.Map;
import lombok.Getter;

@Getter
public enum SlackEvent {

    MESSAGE_CREATED("message", ""),
    MESSAGE_CHANGED("message", "message_changed"),
    MESSAGE_DELETED("message", "message_deleted"),
    CHANNEL_RENAME("channel_rename", ""),
    CHANNEL_DELETED("channel_deleted", ""),
    MEMBER_CHANGED("user_profile_changed", "");

    private final String type;
    private final String subtype;

    SlackEvent(final String type, final String subtype) {
        this.type = type;
        this.subtype = subtype;
    }

    public static SlackEvent of(final Map<String, Object> requestBody) {
        String type = findKey("type", requestBody);
        String subtype = findKey("subtype", requestBody);

        return Arrays.stream(values())
                .filter(slackEvent -> isSameType(slackEvent, type, subtype))
                .findAny()
                .orElseThrow(() -> new SlackEventNotFoundException(type, subtype));
    }

    private static String findKey(final String key, final Map<String, Object> requestBody) {
        if (requestBody.containsKey("event")) {
            Map<String, Object> event = (Map<String, Object>) requestBody.get("event");
            return String.valueOf(event.getOrDefault(key, ""));
        }
        return String.valueOf(requestBody.getOrDefault(key, ""));
    }

    private static boolean isSameType(final SlackEvent event, final String type, final String subtype) {
        return event.type.equals(type) && event.subtype.equals(subtype);
    }
}
