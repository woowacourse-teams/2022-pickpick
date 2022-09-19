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

    public SlackMessageDto toDto() {
        MessageDto message = event.getMessage();

        return new SlackMessageDto(
                message.getUser(),
                message.getClientMsgId(),
                message.getTs(),
                message.getTs(),
                message.getText(),
                event.getChannel()
        );
    }
}
