package com.pickpick.auth.application.dto;

import com.pickpick.workspace.domain.Workspace;
import lombok.Getter;

@Getter
public class BotInfoDto {

    private final String workspaceSlackId;
    private final String botToken;
    private final String botSlackId;

    public BotInfoDto(final String workspaceSlackId, final String botToken, final String botSlackId) {
        this.workspaceSlackId = workspaceSlackId;
        this.botToken = botToken;
        this.botSlackId = botSlackId;
    }

    public Workspace toEntity() {
        return new Workspace(workspaceSlackId, botToken, botSlackId);
    }
}
