package com.pickpick.auth.ui;

import com.pickpick.auth.application.AuthService;
import com.pickpick.auth.ui.dto.LoginResponse;
import javax.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slack-login")
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public LoginResponse login(@RequestParam @NotEmpty final String code) {
        String token = authService.login(code);
        return new LoginResponse(token);
    }
}
