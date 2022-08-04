package com.pickpick.exception;

public class SubscriptionNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "해당 멤버가 구독 중인 채널이 없습니다.";

    public SubscriptionNotFoundException(final Long memberId) {
        super(String.format("%s -> member id: %d", DEFAULT_MESSAGE, memberId));
    }
}
