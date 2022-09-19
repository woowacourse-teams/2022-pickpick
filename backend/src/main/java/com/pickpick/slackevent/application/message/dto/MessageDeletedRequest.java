package com.pickpick.slackevent.application.message.dto;

import lombok.Getter;

@Getter
public class MessageDeletedRequest {

    private MessageDeletedEventDto event;

    private MessageDeletedRequest() {
    }

    public MessageDeletedRequest(final MessageDeletedEventDto event) {
        this.event = event;
    }
}
