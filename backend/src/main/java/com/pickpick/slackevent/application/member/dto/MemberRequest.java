package com.pickpick.slackevent.application.member.dto;

import lombok.Getter;

@Getter
public class MemberRequest {

    private MemberDto event;

    private MemberRequest() {
    }

    public MemberRequest(final MemberDto event) {
        this.event = event;
    }
}
