package com.pickpick.slackevent.application.dto;

import lombok.Getter;

@Getter
public class TypeDto {

    private String type;
    private String subtype;

    private TypeDto() {
    }

    public TypeDto(final String type, final String subtype) {
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
