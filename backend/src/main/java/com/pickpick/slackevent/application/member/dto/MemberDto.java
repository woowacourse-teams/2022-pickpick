package com.pickpick.slackevent.application.member.dto;

import lombok.Getter;

@Getter
public class MemberDto {

    private UserDto user;

    private MemberDto() {
    }

    public MemberDto(final UserDto user) {
        this.user = user;
    }
}
