package com.pickpick.slackevent.application.member.dto;

import lombok.Getter;

@Getter
public class MemberEventDto {

    private UserDto user;

    private MemberEventDto() {
    }

    public MemberEventDto(final UserDto user) {
        this.user = user;
    }
}
