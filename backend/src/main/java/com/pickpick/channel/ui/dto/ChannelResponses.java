package com.pickpick.channel.ui.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ChannelResponses {

    private List<ChannelResponse> channels;

    public ChannelResponses(final List<ChannelResponse> channels) {
        this.channels = channels;
    }
}
