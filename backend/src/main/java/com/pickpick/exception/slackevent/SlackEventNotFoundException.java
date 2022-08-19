package com.pickpick.exception.slackevent;

import com.pickpick.exception.NotFoundException;

public class SlackEventNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "대응하는 enum 값이 없는 Slack Event 요청";
    private static final String CLIENT_MESSAGE = "지원하지 않는 Slack Event 입니다.";

    public SlackEventNotFoundException(final String type, final String subtype) {
        super(String.format("%s -> type: %s, subtype: %s", DEFAULT_MESSAGE, type, subtype));
    }

    @Override
    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
