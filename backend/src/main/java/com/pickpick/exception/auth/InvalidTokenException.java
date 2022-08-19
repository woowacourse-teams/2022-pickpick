package com.pickpick.exception.auth;

import com.pickpick.exception.BadRequestException;

public class InvalidTokenException extends BadRequestException {

    private static final String ERROR_CODE = "INVALID_TOKEN";
    private static final String DEFAULT_MESSAGE = "유효하지 않은 토큰으로 요청";
    private static final String CLIENT_MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException(String token) {
        super(String.format("%s -> token: %s", DEFAULT_MESSAGE, token));
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }

    @Override
    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
