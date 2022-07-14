package com.pickpick.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pickpick.entity.ChannelSubscription;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChannelResponse {

    private Long id;
    private String name;
    @JsonProperty(value = "isSubscribed")
    private boolean subscribed;

    private ChannelResponse() {
    }

    @Builder
    private ChannelResponse(Long id, String name, boolean subscribed) {
        this.id = id;
        this.name = name;
        this.subscribed = subscribed;
    }

    public static ChannelResponse from(final ChannelSubscription channelSubscription) {
        return ChannelResponse.builder()
                .id(channelSubscription.getChannel().getId())
                .name(channelSubscription.getChannel().getName())
                .subscribed(channelSubscription.isSubscribed())
                .build();
    }
}
