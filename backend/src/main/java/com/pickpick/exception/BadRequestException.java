package com.pickpick.exception;

public class BadRequestException extends RuntimeException {

    private static final String CLIENT_MESSAGE = "요청 값이 잘못되었습니다.";

    public BadRequestException(final String message) {
        super(message);
    }

    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
