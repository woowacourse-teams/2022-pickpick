package com.pickpick.exception;

public class BadRequestException extends RuntimeException {

    private String errorCode = "BAD_REQUEST";
    private String clientMessage = "요청 값이 잘못되었습니다.";

    public BadRequestException(final String serverMessage, final String clientMessage, final String errorCode) {
        super(serverMessage);
        this.clientMessage = clientMessage;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getClientMessage() {
        return clientMessage;
    }
}
