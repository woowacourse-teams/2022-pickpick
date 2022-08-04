package com.pickpick.exception;

public class InvalidTokenException extends BadRequestException {

    public InvalidTokenException(final String message) {
        super(message);
    }
}
