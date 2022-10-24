package com.pickpick.auth.application.dto;

import lombok.Getter;

@Getter
public class MemberInfoDto {

    private final String slackId;
    private final String userToken;

    public MemberInfoDto(final String slackId, final String userToken) {
        this.slackId = slackId;
        this.userToken = userToken;
    }
}
