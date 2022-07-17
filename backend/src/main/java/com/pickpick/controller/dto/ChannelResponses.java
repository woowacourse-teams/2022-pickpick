package com.pickpick.controller.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ChannelResponses {

    private List<ChannelResponse> channels;

    public ChannelResponses(final List<ChannelResponse> channels) {
        this.channels = channels;
    }
}
