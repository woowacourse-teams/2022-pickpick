package com.pickpick.controller.dto;

import com.pickpick.entity.ChannelSubscription;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChannelSubscriptionResponse {

    private Long id;
    private String name;
    private int order;

    private ChannelSubscriptionResponse() {
    }

    @Builder
    private ChannelSubscriptionResponse(Long id, String name, int order) {
        this.id = id;
        this.name = name;
        this.order = order;
    }

    public static ChannelSubscriptionResponse from(final ChannelSubscription channelSubscription) {
        return ChannelSubscriptionResponse.builder()
                .id(channelSubscription.getChannel().getId())
                .name(channelSubscription.getChannel().getName())
                .order(channelSubscription.getViewOrder())
                .build();
    }
}
