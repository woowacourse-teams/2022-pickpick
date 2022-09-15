package com.pickpick.slackevent.application.message.dto;

import lombok.Getter;

@Getter
public class MessageCreatedRequest {

    private MessageCreatedDto event;

    private MessageCreatedRequest() {
    }

    public MessageCreatedRequest(final MessageCreatedDto event) {
        this.event = event;
    }
}
