package com.pickpick.exception.channel;

import com.pickpick.exception.BadRequestException;

public class ChannelInvalidNameException extends BadRequestException {

    private static final String DEFAULT_MESSAGE = "유효하지 않은 채널 이름";
    private static final String CLIENT_MESSAGE = "유효하지 않은 채널 이름입니다.";

    public ChannelInvalidNameException(final String name) {
        super(String.format("%s -> channel name: %s", DEFAULT_MESSAGE, name));
    }

    @Override
    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
