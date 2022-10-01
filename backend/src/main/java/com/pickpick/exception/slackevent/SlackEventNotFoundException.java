package com.pickpick.exception.slackevent;

import com.pickpick.exception.NotFoundException;

public class SlackEventNotFoundException extends NotFoundException {

    private static final String ERROR_CODE = "SLACK_EVENT_NOT_FOUND";
    private static final String SERVER_MESSAGE = "대응하는 enum 값이 없는 Slack Event 요청";
    private static final String CLIENT_MESSAGE = "지원하지 않는 Slack Event 입니다.";

    public SlackEventNotFoundException(final String type, final String subtype) {
        super(String.format("%s -> type: %s, subtype: %s", SERVER_MESSAGE, type, subtype), CLIENT_MESSAGE, ERROR_CODE);
    }
}
