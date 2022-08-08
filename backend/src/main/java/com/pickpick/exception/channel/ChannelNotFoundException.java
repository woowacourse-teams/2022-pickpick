package com.pickpick.exception.channel;

import com.pickpick.exception.NotFoundException;

public class ChannelNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "채널을 찾지 못했습니다";

    public ChannelNotFoundException(final Long id) {
        super(String.format("%s -> channel id: %d", DEFAULT_MESSAGE, id));
    }

    public ChannelNotFoundException(final String slackId) {
        super(String.format("%s -> channel slack id: %s", DEFAULT_MESSAGE, slackId));
    }
}
