package com.pickpick.exception;

public class SlackApiCallException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "슬랙 API 호출 실패";

    public SlackApiCallException(final Exception e) {
        super(e);
    }

    public SlackApiCallException(final String apiMethod) {
        super(String.format("%s -> api method : %s", DEFAULT_MESSAGE, apiMethod));
    }

    public SlackApiCallException(final String apiMethod, final String slackErrorMessage) {
        super(String.format("%s -> api method : %s, slack message : %S", DEFAULT_MESSAGE, apiMethod,
                slackErrorMessage));
    }
}
