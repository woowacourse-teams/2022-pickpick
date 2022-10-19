package com.pickpick.exception.auth;

import com.pickpick.exception.BadRequestException;

public class ExpiredTokenException extends BadRequestException {

    private static final String ERROR_CODE = "EXPIRED_TOKEN";
    private static final String SERVER_MESSAGE = "만료된 토큰으로 요청";
    private static final String CLIENT_MESSAGE = "만료된 토큰입니다.";

    public ExpiredTokenException(String token) {
        super(String.format("%s -> token: %s", SERVER_MESSAGE, token), CLIENT_MESSAGE, ERROR_CODE);
    }
}
