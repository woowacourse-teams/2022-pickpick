package com.pickpick.exception;

public class MemberNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "사용자를 찾지 못했습니다";

    public MemberNotFoundException(final Long id) {
        super(String.format("%s -> member id: %d", DEFAULT_MESSAGE, id));
    }

    public MemberNotFoundException(final String slackId) {
        super(String.format("%s -> member id: %s", DEFAULT_MESSAGE, slackId));
    }
}
