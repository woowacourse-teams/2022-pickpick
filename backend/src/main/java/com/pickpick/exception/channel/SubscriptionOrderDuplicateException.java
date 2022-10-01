package com.pickpick.exception.channel;

import com.pickpick.exception.BadRequestException;

public class SubscriptionOrderDuplicateException extends BadRequestException {

    private static final String ERROR_CODE = "SUBSCRIPTION_DUPLICATE";
    private static final String SERVER_MESSAGE = "중복된 구독 순서 요청";
    private static final String CLIENT_MESSAGE = "요청한 구독 순서 내부에 중복이 존재합니다.";

    public SubscriptionOrderDuplicateException() {
        super(SERVER_MESSAGE, CLIENT_MESSAGE, ERROR_CODE);
    }
}
