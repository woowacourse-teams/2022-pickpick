package com.pickpick.slackevent.application.member.dto;

import lombok.Getter;

@Getter
public class UserDto {

    private ProfileDto profile;
    private String id;

    private UserDto() {
    }

    public UserDto(final ProfileDto profile, final String id) {
        this.profile = profile;
        this.id = id;
    }
}
