package com.pickpick.exception.slackevent;

import com.pickpick.exception.NotFoundException;
import com.pickpick.slackevent.application.SlackEvent;

public class SlackEventServiceNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "대응하는 Service 클래스가 없는 Slack Event 요청";;
    private static final String CLIENT_MESSAGE = "지원하지 않는 SlackEvent입니다.";

    public SlackEventServiceNotFoundException(final SlackEvent slackEvent) {
        super(String.format("%s -> type: %s, subtype: %s", DEFAULT_MESSAGE, slackEvent.getType(),
                slackEvent.getSubtype()));
    }

    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
