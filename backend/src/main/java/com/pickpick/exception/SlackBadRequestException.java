package com.pickpick.exception;

public class SlackBadRequestException extends BadRequestException {

    public SlackBadRequestException(final String message) {
        super(message);
    }
}
