package com.pickpick.exception;

public class WrongMessageRequestException extends RuntimeException {
    public WrongMessageRequestException(final String message) {
        super(message);
    }
}
