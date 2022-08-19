package com.pickpick.exception.member;

import com.pickpick.exception.BadRequestException;

public class MemberInvalidUsernameException extends BadRequestException {

    private static final String DEFAULT_MESSAGE = "유효하지 않은 사용자 이름";
    private static final String CLIENT_MESSAGE = "유효하지 않은 사용자 이름입니다.";

    public MemberInvalidUsernameException(final String username) {
        super(String.format("%s -> member username: %s", DEFAULT_MESSAGE, username));
    }

    @Override
    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
