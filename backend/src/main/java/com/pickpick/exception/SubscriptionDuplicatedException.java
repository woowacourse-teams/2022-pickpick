package com.pickpick.exception;

public class SubscriptionDuplicatedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "이미 구독 중인 채널입니다.";

    public SubscriptionDuplicatedException(final Long id) {
        super(String.format("%s -> subscription id: %d", DEFAULT_MESSAGE, id));
    }
}
