package com.pickpick.slackevent.application.message.dto;

import lombok.Getter;

@Getter
public class MessageCreatedRequest {

    private MessageCreatedEventDto event;

    private MessageCreatedRequest() {
    }

    public MessageCreatedRequest(final MessageCreatedEventDto event) {
        this.event = event;
    }
}
