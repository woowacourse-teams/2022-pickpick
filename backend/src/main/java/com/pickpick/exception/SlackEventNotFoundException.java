package com.pickpick.exception;

public class SlackEventNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "지원하지 않는 Slack Event 입니다";

    public SlackEventNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
