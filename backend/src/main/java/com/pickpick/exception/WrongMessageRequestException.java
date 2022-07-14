package com.pickpick.exception;

public class WrongMessageRequestException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "date와 messageId를 동시에 보내는 것은 불가능합니다.";

    public WrongMessageRequestException() {
        super(DEFAULT_MESSAGE);
    }
}
