package com.pickpick.slackevent.application.channel.dto;

import lombok.Getter;

@Getter
public class ChannelDeletedRequest {

    private String channel;
    private String type;

    private ChannelDeletedRequest() {
    }

    public ChannelDeletedRequest(final String channel, final String type) {
        this.channel = channel;
        this.type = type;
    }
}
