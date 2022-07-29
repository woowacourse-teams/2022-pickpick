package com.pickpick.slackevent.application.member.dto;

import lombok.Getter;

@Getter
public class MemberProfileChangedDto {

    private String slackId;
    private String username;
    private String thumbnailUrl;

    public MemberProfileChangedDto(final String slackId, final String username, final String thumbnailUrl) {
        this.slackId = slackId;
        this.username = username;
        this.thumbnailUrl = thumbnailUrl;
    }
}
