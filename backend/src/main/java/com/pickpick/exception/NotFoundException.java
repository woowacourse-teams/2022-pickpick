package com.pickpick.exception;

public class NotFoundException extends RuntimeException {

    private static final String ERROR_CODE = "NOT_FOUND";
    private static final String CLIENT_MESSAGE = "해당 정보를 조회하지 못했습니다.";

    public NotFoundException(final String serverMessage, final String clientMessage, final String errorCode) {
        super(serverMessage);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }

    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
