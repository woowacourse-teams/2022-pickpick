package com.pickpick.controller.dto;

import lombok.Getter;

@Getter
public class ChannelOrderRequest {

    private Long id;
    private int order;

    public ChannelOrderRequest(Long id, int order) {
        this.id = id;
        this.order = order;
    }
}
