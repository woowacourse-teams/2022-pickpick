package com.pickpick.exception;

public class SubscriptionNotExistException extends BadRequestException {

    private static final String DEFAULT_MESSAGE = "구독 중인 채널이 아니라 취소할 수 없습니다.";

    public SubscriptionNotExistException(final Long id) {
        super(String.format("%s -> subscription id: %d", DEFAULT_MESSAGE, id));
    }

    public SubscriptionNotExistException(String message) {
        super(message);
    }
}
