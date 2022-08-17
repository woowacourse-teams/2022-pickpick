package com.pickpick.slackevent.application;

import com.pickpick.exception.slackevent.SlackEventNotFoundException;
import java.util.Arrays;
import java.util.Map;
import lombok.Getter;

@Getter
public enum SlackEvent {

    MESSAGE_CREATED("message", ""),
    MESSAGE_CHANGED("message", "message_changed"),
    MESSAGE_DELETED("message", "message_deleted"),
    MESSAGE_THREAD_BROADCAST("message", "thread_broadcast"),
    MESSAGE_FILE_SHARE("message", "file_share"),
    CHANNEL_RENAME("channel_rename", ""),
    CHANNEL_DELETED("channel_deleted", ""),
    MEMBER_CHANGED("user_profile_changed", ""),
    MEMBER_JOIN("team_join", ""),
    ;

    private final String type;
    private final String subtype;

    SlackEvent(final String type, final String subtype) {
        this.type = type;
        this.subtype = subtype;
    }

    public static SlackEvent of(final Map<String, Object> requestBody) {
        Map<String, Object> event = (Map<String, Object>) requestBody.get("event");
        String type = String.valueOf(event.get("type"));
        String subtype = String.valueOf(event.getOrDefault("subtype", ""));

        return Arrays.stream(values())
                .filter(slackEvent -> isSameType(slackEvent, type, subtype))
                .findAny()
                .orElseThrow(() -> new SlackEventNotFoundException(type, subtype));
    }

    private static boolean isSameType(final SlackEvent event, final String type, final String subtype) {
        return event.type.equals(type) && event.subtype.equals(subtype);
    }

    public boolean isSameSubtype(final String subtype) {
        return this.subtype.equals(subtype);
    }
}
