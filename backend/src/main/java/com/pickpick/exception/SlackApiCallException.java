package com.pickpick.exception;

public class SlackApiCallException extends RuntimeException {

    public SlackApiCallException(final Exception e) {
        super(e);
    }

    public SlackApiCallException(final String message) {
        super(message);
    }
}
