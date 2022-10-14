package com.pickpick.exception.member;

import com.pickpick.exception.NotFoundException;

public class MemberTokenNotFoundException extends NotFoundException {

    private static final String ERROR_CODE = "MEMBER_TOKEN_NOT_FOUND";
    private static final String SERVER_MESSAGE = "유효하지 않은 토큰을 가진 멤버 조회";
    private static final String CLIENT_MESSAGE = "사용자의 토큰을 찾지 못했습니다.";

    public MemberTokenNotFoundException(final long id, final String token) {
        super(String.format("%s -> member id: %d, token: %s", SERVER_MESSAGE, id, token), CLIENT_MESSAGE, ERROR_CODE);
    }
}
