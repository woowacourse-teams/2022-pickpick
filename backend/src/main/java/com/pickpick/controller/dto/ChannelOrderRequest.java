package com.pickpick.controller.dto;

import lombok.Getter;

@Getter
public class ChannelOrderRequest {

    private Long id;
    private int order;

    private ChannelOrderRequest() {
    }

    public ChannelOrderRequest(final Long id, final int order) {
        this.id = id;
        this.order = order;
    }
}
