package com.pickpick.exception;

public class SlackClientException extends RuntimeException {

    public SlackClientException(final Exception e) {
        super(e);
    }
}
