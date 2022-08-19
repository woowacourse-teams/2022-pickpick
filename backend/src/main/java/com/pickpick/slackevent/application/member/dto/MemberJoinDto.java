package com.pickpick.slackevent.application.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberJoinDto {

    private final String slackId;
    private final String username;
    private final String thumbnailUrl;

    @Builder
    public MemberJoinDto(final String slackId, final String username, final String thumbnailUrl) {
        this.slackId = slackId;
        this.username = username;
        this.thumbnailUrl = thumbnailUrl;
    }
}
