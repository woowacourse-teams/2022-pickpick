package com.pickpick.exception;

public class NotFoundException extends RuntimeException {

    private String errorCode = "NOT_FOUND";
    private String clientMessage = "해당 정보를 조회하지 못했습니다.";

    public NotFoundException(final String serverMessage, final String clientMessage, final String errorCode) {
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
