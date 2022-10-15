package com.pickpick.auth.application.dto;

import com.pickpick.workspace.domain.Workspace;
import lombok.Getter;

@Getter
public class WorkspaceInfoDto {

    private final String workspaceSlackId;
    private final String botToken;
    private final String botSlackId;

    public WorkspaceInfoDto(final String workspaceSlackId, final String botToken, final String botSlackId) {
        this.workspaceSlackId = workspaceSlackId;
        this.botToken = botToken;
        this.botSlackId = botSlackId;
    }

    public Workspace toEntity() {
        return new Workspace(workspaceSlackId, botToken, botSlackId);
    }
}
