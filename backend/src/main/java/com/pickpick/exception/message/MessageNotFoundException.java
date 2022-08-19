package com.pickpick.exception.message;

import com.pickpick.exception.NotFoundException;

public class MessageNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "존재하지 않는 메시지 조회";
    private static final String CLIENT_MESSAGE = "메시지를 찾지 못했습니다.";

    public MessageNotFoundException(final Long id) {
        super(String.format("%s -> message id: %d", DEFAULT_MESSAGE, id));
    }

    public MessageNotFoundException(final String slackId) {
        super(String.format("%s -> message slack id: %s", DEFAULT_MESSAGE, slackId));
    }

    @Override
    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
