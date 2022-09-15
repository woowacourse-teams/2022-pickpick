package com.pickpick.slackevent.application.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PreviousMessageDto {

    private String clientMsgId;

    private PreviousMessageDto() {
    }

    public PreviousMessageDto(final String clientMsgId) {
        this.clientMsgId = clientMsgId;
    }
}
