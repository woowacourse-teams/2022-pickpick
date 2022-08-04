package com.pickpick.exception;

public class SubscriptionInvalidOrderException extends BadRequestException {

    private static final String DEFAULT_MESSAGE = "구독 순서는 1 이상이여야합니다.";

    public SubscriptionInvalidOrderException() {
        super(DEFAULT_MESSAGE);
    }
}
