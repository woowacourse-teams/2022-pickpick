package com.pickpick.slackevent.application.message.dto;

import lombok.Getter;

@Getter
public class MessageDeletedRequest {

    private MessageDeletedDto event;

    private MessageDeletedRequest() {
    }

    public MessageDeletedRequest(final MessageDeletedDto event) {
        this.event = event;
    }
}
