package com.pickpick.exception.channel;

import com.pickpick.exception.BadRequestException;

public class SubscriptionDuplicateException extends BadRequestException {

    private static final String DEFAULT_MESSAGE = "이미 구독 중인 채널입니다.";

    public SubscriptionDuplicateException(final Long id) {
        super(String.format("%s -> subscription id: %d", DEFAULT_MESSAGE, id));
    }
}
