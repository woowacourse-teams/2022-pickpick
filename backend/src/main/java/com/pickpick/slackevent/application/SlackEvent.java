package com.pickpick.slackevent.application;

import com.pickpick.exception.slackevent.SlackEventNotFoundException;
import com.pickpick.slackevent.application.dto.SlackEventDto;
import com.pickpick.utils.JsonUtils;
import java.util.Arrays;
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

    public static SlackEvent of(final String requestBody) {
        SlackEventDto request = JsonUtils.convert(requestBody, SlackEventDto.class);
        String type = request.getEvent().getType();
        String subtype = request.getEvent().getSubtype();

        return Arrays.stream(values())
                .filter(slackEvent -> slackEvent.isSameType(type, subtype))
                .findAny()
                .orElseThrow(() -> new SlackEventNotFoundException(type, subtype));
    }

    private boolean isSameType(final String type, final String subtype) {
        return this.type.equals(type) && this.subtype.equals(subtype);
    }

    public boolean isSameSubtype(final String subtype) {
        return this.subtype.equals(subtype);
    }
}
