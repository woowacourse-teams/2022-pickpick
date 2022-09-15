package com.pickpick.slackevent.application.message.dto;

import lombok.Getter;

@Getter
public class MessageChangedRequest {

    private MessageChangedDto event;

    private MessageChangedRequest() {
    }

    public MessageChangedRequest(final MessageChangedDto event) {
        this.event = event;
    }
}
