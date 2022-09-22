package com.pickpick.slackevent.application.channel.dto;

import lombok.Getter;

@Getter
public class ChannelDeletedRequest {

    private ChannelDeletedEventDto event;

    private ChannelDeletedRequest() {
    }

    public ChannelDeletedRequest(final ChannelDeletedEventDto event) {
        this.event = event;
    }
}
