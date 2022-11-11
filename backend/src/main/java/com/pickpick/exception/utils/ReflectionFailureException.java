package com.pickpick.exception.utils;

public class ReflectionFailureException extends RuntimeException {

    private static final String SERVER_MESSAGE = "리플렉션 중 오류 발생";

    public ReflectionFailureException() {
        super(SERVER_MESSAGE);
    }
}
