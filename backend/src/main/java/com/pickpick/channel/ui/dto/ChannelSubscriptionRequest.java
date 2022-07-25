package com.pickpick.channel.ui.dto;

import lombok.Getter;

@Getter
public class ChannelSubscriptionRequest {

    private Long id;

    private ChannelSubscriptionRequest() {
    }

    public ChannelSubscriptionRequest(Long id) {
        this.id = id;
    }
}
