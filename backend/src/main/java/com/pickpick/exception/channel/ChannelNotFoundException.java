package com.pickpick.exception.channel;

import com.pickpick.exception.NotFoundException;

public class ChannelNotFoundException extends NotFoundException {

    private static final String ERROR_CODE = "CHANNEL_NOT_FOUND";
    private static final String DEFAULT_MESSAGE = "존재하지 않는 채널 조회";
    private static final String CLIENT_MESSAGE = "채널을 찾지 못했습니다.";

    public ChannelNotFoundException(final Long id) {
        super(String.format("%s -> channel id: %d", DEFAULT_MESSAGE, id));
    }

    public ChannelNotFoundException(final String slackId) {
        super(String.format("%s -> channel slack id: %s", DEFAULT_MESSAGE, slackId));
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
