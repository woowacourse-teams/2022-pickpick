package com.pickpick.exception.member;

import com.pickpick.exception.BadRequestException;

public class MemberInvalidUsernameException extends BadRequestException {

    private static final String ERROR_CODE = "MEMBER_INVALID_USERNAME";
    private static final String SERVER_MESSAGE = "유효하지 않은 사용자 이름";
    private static final String CLIENT_MESSAGE = "유효하지 않은 사용자 이름입니다.";

    public MemberInvalidUsernameException(final String username) {
        super(String.format("%s -> member username: %s", SERVER_MESSAGE, username), CLIENT_MESSAGE, ERROR_CODE);
    }
}
