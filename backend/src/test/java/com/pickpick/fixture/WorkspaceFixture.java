package com.pickpick.fixture;

import com.pickpick.workspace.domain.Workspace;

public enum WorkspaceFixture {

    JUPJUP("T0001", "xoxb-token-0001"),
    ;

    private final String slackId;
    private final String botToken;

    WorkspaceFixture(final String slackId, final String botToken) {
        this.slackId = slackId;
        this.botToken = botToken;
    }

    public Workspace create() {
        return new Workspace(slackId, botToken);
    }

    public String getSlackId() {
        return slackId;
    }

}
