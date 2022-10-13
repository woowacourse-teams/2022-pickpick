package com.pickpick.exception.auth;

import com.pickpick.exception.BadRequestException;

public class ExtractTokenFailException extends BadRequestException {

    private static final String ERROR_CODE = "INVALID_TOKEN";
    private static final String SERVER_MESSAGE = "토큰 추출 실패";
    private static final String CLIENT_MESSAGE = "유효하지 않은 토큰입니다.";

    public ExtractTokenFailException() {
        super(SERVER_MESSAGE, CLIENT_MESSAGE, ERROR_CODE);
    }
}
