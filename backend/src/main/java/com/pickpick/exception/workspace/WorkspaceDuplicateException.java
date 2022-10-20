package com.pickpick.exception.workspace;

import com.pickpick.exception.BadRequestException;

public class WorkspaceDuplicateException extends BadRequestException {

    private static final String ERROR_CODE = "WORKSPACE_DUPLICATE";
    private static final String SERVER_MESSAGE = "이미 존재하는 워크스페이스 등록 시도";
    private static final String CLIENT_MESSAGE = "이미 등록된 워크스페이스입니다.";

    public WorkspaceDuplicateException(final String slackId) {
        super(String.format("%s -> workspace slack id: %s", SERVER_MESSAGE, slackId), CLIENT_MESSAGE, ERROR_CODE);
    }
}
