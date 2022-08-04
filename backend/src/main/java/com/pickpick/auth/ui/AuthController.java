package com.pickpick.auth.ui;

import com.pickpick.auth.application.AuthService;
import com.pickpick.auth.ui.dto.LoginResponse;
import com.pickpick.utils.AuthorizationExtractor;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/slack-login")
    public LoginResponse login(@RequestParam @NotEmpty final String code) {
        return authService.login(code);
    }

    @GetMapping("/certification")
    public void verifyToken(final HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request);
        authService.verifyToken(token);
    }
}
