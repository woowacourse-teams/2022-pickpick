package com.pickpick.slackevent.domain;

import java.util.Map;

public class Participation {

    private final Map<String, Boolean> value;

    public Participation(final Map<String, Boolean> value) {
        this.value = value;
    }

    public boolean isParticipant(final String channelSlackId) {
        return value.getOrDefault(channelSlackId, false);
    }
}
