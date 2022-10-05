package com.pickpick.exception.workspace;

import com.pickpick.exception.NotFoundException;

public class WorkspaceNotFoundException extends NotFoundException {

    private static final String ERROR_CODE = "WORKSPACE_NOT_FOUND";
    private static final String SERVER_MESSAGE = "존재하지 않는 워크스페이스 조회";
    private static final String CLIENT_MESSAGE = "워크스페이스를 찾지 못했습니다.";

    public WorkspaceNotFoundException(final String slackId) {
        super(String.format("%s -> workspace slack id: %s", SERVER_MESSAGE, slackId), CLIENT_MESSAGE, ERROR_CODE);
    }
}
