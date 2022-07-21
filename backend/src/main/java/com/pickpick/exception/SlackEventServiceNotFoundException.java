package com.pickpick.exception;

import com.pickpick.controller.event.SlackEvent;

public class SlackEventServiceNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "Service를 지원하지 않는 SlackEvent입니다.";

    public SlackEventServiceNotFoundException(final SlackEvent slackEvent) {
        super(String.format("%s -> type: %s, subtype: %s", DEFAULT_MESSAGE, slackEvent.getType(),
                slackEvent.getSubtype()));
    }
}
