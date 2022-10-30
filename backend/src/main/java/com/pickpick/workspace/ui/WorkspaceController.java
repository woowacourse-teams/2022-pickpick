package com.pickpick.workspace.ui;

import com.pickpick.auth.application.AuthService;
import com.pickpick.auth.application.dto.OAuthAccessInfoDto;
import com.pickpick.auth.ui.dto.LoginResponse;
import com.pickpick.workspace.application.WorkspaceService;
import javax.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final AuthService authService;

    public WorkspaceController(final WorkspaceService workspaceService, final AuthService authService) {
        this.workspaceService = workspaceService;
        this.authService = authService;
    }

    @GetMapping("/slack-workspace")
    public LoginResponse register(@RequestParam @NotEmpty final String code) {
        OAuthAccessInfoDto oAuthAccessInfoDto = workspaceService.register(code);
        return authService.login(oAuthAccessInfoDto);
    }
}
