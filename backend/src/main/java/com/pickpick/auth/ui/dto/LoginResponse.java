package com.pickpick.auth.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private String token;

    @JsonProperty("isFirstLogin")
    private boolean firstLogin;

    @Builder
    public LoginResponse(final String token, final boolean firstLogin) {
        this.token = token;
        this.firstLogin = firstLogin;
    }
}
