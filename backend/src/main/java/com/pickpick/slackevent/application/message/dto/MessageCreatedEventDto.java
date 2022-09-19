package com.pickpick.slackevent.application.message.dto;

import lombok.Getter;

@Getter
public class MessageCreatedEventDto {

    private String subtype;
    private String user;
    private String ts;
    private String text;
    private String channel;
    private String clientMsgId;
    private String threadTs;

    private MessageCreatedEventDto() {
    }

    public MessageCreatedEventDto(final String subtype, final String user, final String clientMsgId, final String ts,
                                  final String threadTs, final String text, final String channel) {
        this.subtype = subtype;
        this.user = user;
        this.clientMsgId = clientMsgId;
        this.ts = ts;
        this.threadTs = threadTs;
        this.text = text;
        this.channel = channel;
    }

    public String getText() {
        if (text == null) {
            return "";
        }
        return text;
    }
}
