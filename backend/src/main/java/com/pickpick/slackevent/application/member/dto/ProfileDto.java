package com.pickpick.slackevent.application.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ProfileDto {

    private String displayName;
    private String realName;
    @JsonProperty(value = "image_512")
    private String image512;

    private ProfileDto() {
    }

    public ProfileDto(final String displayName, final String realName, final String image512) {
        this.displayName = displayName;
        this.realName = realName;
        this.image512 = image512;
    }
}
