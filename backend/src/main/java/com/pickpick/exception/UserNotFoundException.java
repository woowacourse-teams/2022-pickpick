package com.pickpick.exception;

public class UserNotFoundException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "사용자를 찾지 못했습니다";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
