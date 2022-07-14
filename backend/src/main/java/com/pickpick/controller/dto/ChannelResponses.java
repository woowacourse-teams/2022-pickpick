package com.pickpick.controller.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ChannelResponses {

    private List<ChannelResponse> channels;

    public ChannelResponses(List<ChannelResponse> channels) {
        this.channels = channels;
    }
}
