package com.pickpick.exception.channel;

import com.pickpick.exception.BadRequestException;

public class SubscriptionInvalidOrderException extends BadRequestException {

    private static final String DEFAULT_MESSAGE = "구독 순서에 0 이하의 수 입력";
    private static final String CLIENT_MESSAGE = "구독 순서는 1 이상이여야합니다.";

    public SubscriptionInvalidOrderException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
