package com.pickpick.auth.application.dto;

import com.pickpick.workspace.domain.Workspace;
import lombok.Getter;

@Getter
public class BotInfoDto {

    private final String workspaceSlackId;
    private final String botToken;

    public BotInfoDto(final String workspaceSlackId, final String botToken) {
        this.workspaceSlackId = workspaceSlackId;
        this.botToken = botToken;
    }

    public Workspace toEntity() {
        return new Workspace(workspaceSlackId, botToken);
    }
}
