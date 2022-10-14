package com.pickpick.fixture;

import com.pickpick.workspace.domain.Workspace;

public enum WorkspaceFixture {

    JUPJUP("T0001", "xoxb-token-0001", "UB000001"),
    ;

    private final String slackId;
    private final String botToken;
    private final String botUserId;

    WorkspaceFixture(final String slackId, final String botToken, final String botUserId) {
        this.slackId = slackId;
        this.botToken = botToken;
        this.botUserId = botUserId;
    }

    public Workspace create() {
        return new Workspace(slackId, botToken, botUserId);
    }

    public String getSlackId() {
        return slackId;
    }

}
