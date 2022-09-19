package com.pickpick.slackevent.application.message.dto;

import lombok.Getter;

@Getter
public class MessageChangedRequest {

    private MessageChangedEventDto event;

    private MessageChangedRequest() {
    }

    public MessageChangedRequest(final MessageChangedEventDto event) {
        this.event = event;
    }
}
