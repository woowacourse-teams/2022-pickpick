package com.pickpick.slackevent.application.channel.dto;

import lombok.Getter;

@Getter
public class ChannelDeletedEventDto {

    private String channel;
    private String type;

    private ChannelDeletedEventDto() {
    }

    public ChannelDeletedEventDto(final String channel, final String type) {
        this.channel = channel;
        this.type = type;
    }
}
