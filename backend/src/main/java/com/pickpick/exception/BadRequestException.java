package com.pickpick.exception;

public class BadRequestException extends RuntimeException {

    private static final String ERROR_CODE = "BAD_REQUEST";
    private static final String CLIENT_MESSAGE = "요청 값이 잘못되었습니다.";

    public BadRequestException(final String message) {
        super(message);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }

    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
