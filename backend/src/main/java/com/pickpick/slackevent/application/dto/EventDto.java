package com.pickpick.slackevent.application.dto;

import lombok.Getter;

@Getter
public class EventDto {

    private String type;
    private String subtype;

    private EventDto() {
    }

    public EventDto(final String type, final String subtype) {
        this.type = type;
        this.subtype = subtype;
    }

    public String getSubtype() {
        if (subtype == null) {
            return "";
        }
        return subtype;
    }
}
