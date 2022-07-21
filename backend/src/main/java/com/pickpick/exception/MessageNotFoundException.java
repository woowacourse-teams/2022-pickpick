package com.pickpick.exception;

public class MessageNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "메시지를 찾지 못했습니다";

    public MessageNotFoundException(final Long id) {
        super(String.format("%s -> message id: %d", DEFAULT_MESSAGE, id));
    }

    public MessageNotFoundException(final String slackId) {
        super(String.format("%s -> message slack id: %s", DEFAULT_MESSAGE, slackId));
    }
}
