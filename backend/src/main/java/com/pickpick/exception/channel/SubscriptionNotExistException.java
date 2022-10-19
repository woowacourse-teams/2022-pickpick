package com.pickpick.exception.channel;

import com.pickpick.exception.BadRequestException;

public class SubscriptionNotExistException extends BadRequestException {

    private static final String ERROR_CODE = "SUBSCRIPTION_NOT_EXIST";
    private static final String SERVER_MESSAGE = "구독 중이 아닌 채널을 구독 조회";
    private static final String CLIENT_MESSAGE = "구독 중인 채널이 아니라 취소할 수 없습니다.";

    public SubscriptionNotExistException(final Long channelId) {
        super(String.format("%s -> subscription channel id: %d", SERVER_MESSAGE, channelId), CLIENT_MESSAGE,
                ERROR_CODE);
    }

    public SubscriptionNotExistException(final String message) {
        super(message, CLIENT_MESSAGE, ERROR_CODE);
    }
}
