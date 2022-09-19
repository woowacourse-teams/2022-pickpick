package com.pickpick.slackevent.application.member.dto;

import lombok.Getter;

@Getter
public class MemberRequest {

    private MemberEventDto event;

    private MemberRequest() {
    }

    public MemberRequest(final MemberEventDto event) {
        this.event = event;
    }
}
