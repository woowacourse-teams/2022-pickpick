package com.pickpick.slackevent.application.message.dto;

import lombok.Getter;

@Getter
public class MessageDeletedEventDto {

    private PreviousMessageDto previousMessage;

    private MessageDeletedEventDto() {
    }

    public MessageDeletedEventDto(final PreviousMessageDto previousMessage) {
        this.previousMessage = previousMessage;
    }
}
