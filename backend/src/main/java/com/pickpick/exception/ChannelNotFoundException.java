package com.pickpick.exception;

public class ChannelNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "채널을 찾지 못했습니다";

    public ChannelNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
