package com.pickpick.exception.member;

import com.pickpick.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    private static final String ERROR_CODE = "MEMBER_NOT_FOUND";
    private static final String SERVER_MESSAGE = "존재하지 않는 멤버 조회";
    private static final String TOKEN_SERVER_MESSAGE = "유효하지 않은 토큰을 가진 멤버 조회";
    private static final String CLIENT_MESSAGE = "사용자를 찾지 못했습니다.";

    public MemberNotFoundException(final Long id) {
        super(String.format("%s -> member id: %d", SERVER_MESSAGE, id), CLIENT_MESSAGE, ERROR_CODE);
    }

    public MemberNotFoundException(final String slackId) {
        super(String.format("%s -> member slack id: %s", SERVER_MESSAGE, slackId), CLIENT_MESSAGE, ERROR_CODE);
    }

    public MemberNotFoundException(final long id, final String token) {
        super(String.format("%s -> member id: %d, token: %s", TOKEN_SERVER_MESSAGE, id, token), CLIENT_MESSAGE,
                ERROR_CODE);
    }
}
