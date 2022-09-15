package com.pickpick.slackevent.application.message.dto;

import lombok.Getter;

@Getter
public class MessageChangedDto {

    private MessageDto message;
    private String channel;

    private MessageChangedDto() {
    }

    public MessageChangedDto(final MessageDto message, final String channel) {
        this.message = message;
        this.channel = channel;
    }
}
