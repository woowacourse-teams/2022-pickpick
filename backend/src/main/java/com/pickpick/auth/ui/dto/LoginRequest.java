package com.pickpick.auth.ui.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String error;
    private String code;

    public LoginRequest(final String error, final String code) {
        this.error = error;
        this.code = code;
    }
}
