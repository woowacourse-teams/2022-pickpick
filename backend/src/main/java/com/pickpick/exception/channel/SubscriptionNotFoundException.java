package com.pickpick.exception.channel;

import com.pickpick.exception.NotFoundException;

public class SubscriptionNotFoundException extends NotFoundException {

    private static final String ERROR_CODE = "SUBSCRIPTION_NOT_FOUND";
    private static final String DEFAULT_MESSAGE = "채널 구독이 0개인 멤버로 구독 목록 조회";
    private static final String CLIENT_MESSAGE = "해당 멤버가 구독 중인 채널이 없습니다.";

    public SubscriptionNotFoundException(final Long memberId) {
        super(String.format("%s -> subscription member id: %d", DEFAULT_MESSAGE, memberId));
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
