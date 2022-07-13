package com.pickpick.controller.dto;

import com.pickpick.entity.Channel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChannelResponse {
    private Long id;
    private String channelSlackId;
    private String name;

    private ChannelResponse() {
    }

    @Builder
    private ChannelResponse(Long id, String channelSlackId, String name) {
        this.id = id;
        this.channelSlackId = channelSlackId;
        this.name = name;
    }

    public static ChannelResponse from(final Channel channel) {
        return ChannelResponse.builder()
                .id(channel.getId())
                .channelSlackId(channel.getSlackId())
                .name(channel.getName())
                .build();
    }
}
