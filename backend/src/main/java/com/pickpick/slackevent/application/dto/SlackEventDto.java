package com.pickpick.slackevent.application.dto;

import lombok.Getter;

@Getter
public class SlackEventDto {

    private TypeDto event;

    private SlackEventDto() {
    }

    public SlackEventDto(final TypeDto event) {
        this.event = event;
    }
}
