package com.pickpick.exception;

public class SubscriptionOrderMinException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "구독 순서는 1 이상이여야합니다.";

    public SubscriptionOrderMinException() {
        super(DEFAULT_MESSAGE);
    }
}
