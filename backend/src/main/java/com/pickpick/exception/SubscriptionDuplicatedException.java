package com.pickpick.exception;

public class SubscriptionDuplicatedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "이미 구독 중인 채널입니다.";

    public SubscriptionDuplicatedException() {
        super(DEFAULT_MESSAGE);
    }

    public SubscriptionDuplicatedException(final Long slackId) {
        super(String.format("%s : %s", DEFAULT_MESSAGE, slackId));
    }
}
