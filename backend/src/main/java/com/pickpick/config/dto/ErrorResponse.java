package com.pickpick.config.dto;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private String code;
    private String message;

    private ErrorResponse() {
    }

    public ErrorResponse(final String code, final String message) {
        this.code = code;
        this.message = message;
    }
}
