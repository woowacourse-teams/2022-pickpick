package com.pickpick.slackevent.application.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MessageDto {

    private String subtype;
    private String user;
    private String ts;
    private String text;
    private String channel;
    private String clientMsgId;

    private MessageDto() {
    }

    public MessageDto(final String subtype, final String user, final String clientMsgId, final String ts,
                                 final String text, final String channel) {
        this.subtype = subtype;
        this.user = user;
        this.clientMsgId = clientMsgId;
        this.ts = ts;
        this.text = text;
        this.channel = channel;
    }
}
