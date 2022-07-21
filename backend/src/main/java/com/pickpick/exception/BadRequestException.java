package com.pickpick.exception;

public class BadRequestException extends RuntimeException{

    public BadRequestException(final String message) {
        super(message);
    }
}
