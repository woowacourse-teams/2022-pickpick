package com.pickpick.exception;

public class SubscriptionNotExistException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "구독 중인 채널이 아니라 취소할 수 없습니다.";

    public SubscriptionNotExistException() {
        super(DEFAULT_MESSAGE);
    }

    public SubscriptionNotExistException(final Long slackId) {
        super(String.format("%s : %s", DEFAULT_MESSAGE, slackId));
    }
}
