package com.pickpick.exception.auth;

import com.pickpick.exception.BadRequestException;

public class InvalidTokenException extends BadRequestException {

    public InvalidTokenException(final String message) {
        super(message);
    }
}
