package com.pickpick.exception.utils;

import com.pickpick.exception.BadRequestException;

public class InvalidJsonRequestException extends BadRequestException {

    private static final String DEFAULT_MESSAGE = "유효하지 않은 json 형식";
    private static final String CLIENT_MESSAGE = "잘못된 요청 값입니다.";

    public InvalidJsonRequestException(final String json) {
        super(String.format("%s -> json input: %s", DEFAULT_MESSAGE, json));
    }

    @Override
    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
