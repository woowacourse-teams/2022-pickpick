package com.pickpick.exception.message;

import com.pickpick.exception.NotFoundException;

public class MessageNotFoundException extends NotFoundException {

    private static final String ERROR_CODE = "MESSAGE_NOT_FOUND";
    private static final String SERVER_MESSAGE = "존재하지 않는 메시지 조회";
    private static final String CLIENT_MESSAGE = "메시지를 찾지 못했습니다.";

    public MessageNotFoundException(final Long id) {
        super(String.format("%s -> message id: %d", SERVER_MESSAGE, id), CLIENT_MESSAGE, ERROR_CODE);
    }

    public MessageNotFoundException(final String slackId) {
        super(String.format("%s -> message slack id: %s", SERVER_MESSAGE, slackId), CLIENT_MESSAGE, ERROR_CODE);
    }
}
