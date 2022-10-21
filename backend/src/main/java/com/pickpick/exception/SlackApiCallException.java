package com.pickpick.exception;

import com.slack.api.methods.SlackApiTextResponse;

public class SlackApiCallException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "슬랙 API 호출 실패";

    public SlackApiCallException(final String apiMethod) {
        super(String.format("%s -> api method : %s", DEFAULT_MESSAGE, apiMethod));
    }

    public SlackApiCallException(final String apiMethod, final SlackApiTextResponse response) {
        super(String.format("%s -> api method : %s, slack message : %s, slack needed: %s, slack provided: %s",
                DEFAULT_MESSAGE,
                apiMethod,
                response.getError(),
                response.getNeeded(),
                response.getProvided()
        ));
    }
}
