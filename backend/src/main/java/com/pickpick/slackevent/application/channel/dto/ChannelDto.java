package com.pickpick.slackevent.application.channel.dto;

import lombok.Getter;

@Getter
public class ChannelDto {

    private String id;
    private String name;

    private ChannelDto() {
    }

    public ChannelDto(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
}
