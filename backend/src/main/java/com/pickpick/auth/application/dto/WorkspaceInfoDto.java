package com.pickpick.auth.application.dto;

import com.pickpick.workspace.domain.Workspace;
import lombok.Getter;

@Getter
public class WorkspaceInfoDto {

    private final String workspaceSlackId;
    private final String botToken;
    private final String botSlackId;
    private final String userToken;

    public WorkspaceInfoDto(final String workspaceSlackId, final String botToken, final String botSlackId,
                            final String userToken) {
        this.workspaceSlackId = workspaceSlackId;
        this.botToken = botToken;
        this.botSlackId = botSlackId;
        this.userToken = userToken;
    }

    public Workspace toEntity() {
        return new Workspace(workspaceSlackId, botToken, botSlackId);
    }
}
