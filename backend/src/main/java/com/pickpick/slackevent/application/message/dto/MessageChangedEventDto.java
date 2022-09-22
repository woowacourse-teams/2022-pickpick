package com.pickpick.slackevent.application.message.dto;

import lombok.Getter;

@Getter
public class MessageChangedEventDto {

    private MessageDto message;
    private String channel;

    private MessageChangedEventDto() {
    }

    public MessageChangedEventDto(final MessageDto message, final String channel) {
        this.message = message;
        this.channel = channel;
    }
}
