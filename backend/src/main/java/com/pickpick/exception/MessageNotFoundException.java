package com.pickpick.exception;

public class MessageNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "메시지를 찾지 못했습니다";

    public MessageNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
