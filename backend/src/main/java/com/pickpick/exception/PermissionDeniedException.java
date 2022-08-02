package com.pickpick.exception;

public class PermissionDeniedException extends UnauthorizedException {

    private static final String DEFAULT_MESSAGE = "엑세스 권한 요청이 거부되었습니다.";

    public PermissionDeniedException() {
        super(DEFAULT_MESSAGE);
    }
}
