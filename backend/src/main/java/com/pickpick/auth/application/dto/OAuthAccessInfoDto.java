package com.pickpick.auth.application.dto;

import com.pickpick.workspace.domain.Workspace;
import lombok.Getter;

@Getter
public class OAuthAccessInfoDto {

    private final String workspaceSlackId;
    private final String botToken;
    private final String botSlackId;
    private final String userToken;
    private final String userSlackId;

    public OAuthAccessInfoDto(final String workspaceSlackId, final String botToken, final String botSlackId,
                              final String userToken, final String userSlackId) {
        this.workspaceSlackId = workspaceSlackId;
        this.botToken = botToken;
        this.botSlackId = botSlackId;
        this.userToken = userToken;
        this.userSlackId = userSlackId;
    }

    public Workspace toEntity() {
        return new Workspace(workspaceSlackId, botToken, botSlackId);
    }
}
