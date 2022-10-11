package com.pickpick.slackevent.application.member.dto;

import com.pickpick.member.domain.Member;
import com.pickpick.workspace.domain.Workspace;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberJoinDto {

    private final String slackId;
    private final String username;
    private final String thumbnailUrl;
    private final String workspaceSlackId;

    @Builder
    public MemberJoinDto(final String slackId, final String username, final String thumbnailUrl,
                         final String workspaceSlackId) {
        this.slackId = slackId;
        this.username = username;
        this.thumbnailUrl = thumbnailUrl;
        this.workspaceSlackId = workspaceSlackId;
    }

    public Member toEntity(final Workspace workspace) {
        return new Member(slackId, username, thumbnailUrl, workspace);
    }
}
