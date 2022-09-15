package com.pickpick.slackevent.application.message.dto;

import lombok.Getter;

@Getter
public class MessageDeletedDto {

    private PreviousMessageDto previousMessage;

    private MessageDeletedDto() {
    }

    public MessageDeletedDto(final PreviousMessageDto previousMessage) {
        this.previousMessage = previousMessage;
    }
}
