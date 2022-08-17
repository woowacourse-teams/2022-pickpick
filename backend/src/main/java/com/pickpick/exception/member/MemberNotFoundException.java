package com.pickpick.exception.member;

import com.pickpick.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    private static final String ERROR_CODE = "MEMBER_NOT_FOUND";
    private static final String DEFAULT_MESSAGE = "존재하지 않는 멤버 조회";
    private static final String CLIENT_MESSAGE = "사용자를 찾지 못했습니다.";

    public MemberNotFoundException(final Long id) {
        super(String.format("%s -> member id: %d", DEFAULT_MESSAGE, id));
    }

    public MemberNotFoundException(final String slackId) {
        super(String.format("%s -> member slack id: %s", DEFAULT_MESSAGE, slackId));
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
