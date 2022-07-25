package com.pickpick.channel.ui.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ChannelSubscriptionResponses {

    private List<ChannelSubscriptionResponse> channels;

    public ChannelSubscriptionResponses(List<ChannelSubscriptionResponse> channels) {
        this.channels = channels;
    }
}
