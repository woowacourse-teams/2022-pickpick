package com.pickpick.channel.ui.dto;

import lombok.Getter;

@Getter
public class ChannelSubscriptionRequest {

    private Long channelId;

    private ChannelSubscriptionRequest() {
    }

    public ChannelSubscriptionRequest(Long channelId) {
        this.channelId = channelId;
    }
}
