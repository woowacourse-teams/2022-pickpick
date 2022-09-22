package com.pickpick.slackevent.application.channel.dto;

import lombok.Getter;

@Getter
public class ChannelEventDto {

    private ChannelDto channel;

    private ChannelEventDto() {
    }

    public ChannelEventDto(final ChannelDto channel) {
        this.channel = channel;
    }
}
