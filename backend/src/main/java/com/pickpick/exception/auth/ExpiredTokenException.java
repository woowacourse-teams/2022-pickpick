package com.pickpick.exception.auth;

import com.pickpick.exception.BadRequestException;

public class ExpiredTokenException extends BadRequestException {

    private static final String DEFAULT_MESSAGE = "만료된 토큰으로 요청";
    private static final String CLIENT_MESSAGE = "만료된 토큰입니다.";

    public ExpiredTokenException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
