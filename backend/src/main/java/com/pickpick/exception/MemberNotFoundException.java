package com.pickpick.exception;

public class MemberNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "사용자를 찾지 못했습니다";

    public MemberNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
