package com.pickpick.slackevent.application.dto;

import lombok.Getter;

@Getter
public class SlackEventRequest {

    private EventDto event;

    private SlackEventRequest() {
    }

    public SlackEventRequest(final EventDto event) {
        this.event = event;
    }
}
