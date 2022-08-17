package com.pickpick.exception.channel;

import com.pickpick.exception.BadRequestException;

public class SubscriptionOrderDuplicateException extends BadRequestException {

    private static final String ERROR_CODE = "SUBSCRIPTION_DUPLICATE";
    private static final String DEFAULT_MESSAGE = "중복된 구독 순서 요청";
    private static final String CLIENT_MESSAGE = "요청한 구독 순서 내부에 중복이 존재합니다.";

    public SubscriptionOrderDuplicateException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }

    @Override
    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
