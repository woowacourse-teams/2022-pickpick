package com.pickpick.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ChannelResponse {

    private Long id;
    private String name;
    @JsonProperty(value = "isSubscribed")
    private boolean subscribed;

    private ChannelResponse() {
    }

    public ChannelResponse(Long id, String name, boolean subscribed) {
        this.id = id;
        this.name = name;
        this.subscribed = subscribed;
    }
}