package com.pickpick.exception.utils;

import com.pickpick.exception.BadRequestException;

public class InvalidJsonRequestException extends BadRequestException {

    private static final String ERROR_CODE = "INVALID_JSON_REQUEST";
    private static final String SERVER_MESSAGE = "유효하지 않은 json 형식";
    private static final String CLIENT_MESSAGE = "잘못된 요청 값입니다.";

    public InvalidJsonRequestException(final String json) {
        super(String.format("%s -> json input: %s", SERVER_MESSAGE, json), CLIENT_MESSAGE, ERROR_CODE);
    }
}
