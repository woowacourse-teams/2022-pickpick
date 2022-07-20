package com.pickpick.exception;

public class DuplicatedSubscriptionException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "이미 구독 중인 채널입니다.";

    public DuplicatedSubscriptionException() {
        super(DEFAULT_MESSAGE);
    }
}
