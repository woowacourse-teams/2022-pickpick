package com.pickpick.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pickpick.entity.Channel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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

    public static ChannelResponse of(final List<Channel> subscribedChannels, final Channel channel) {
        return ChannelResponse.builder()
                .id(channel.getId())
                .name(channel.getName())
                .subscribed(subscribedChannels.contains(channel))
                .build();
    }
}
