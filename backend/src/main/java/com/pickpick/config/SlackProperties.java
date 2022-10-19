package com.pickpick.config;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "slack")
public class SlackProperties {

    @NotBlank
    private final String clientId;

    @NotBlank
    private final String clientSecret;

    @NotBlank
    private final String loginRedirectUrl;

    @NotBlank
    private final String workspaceRedirectUrl;

    public SlackProperties(final String clientId, final String clientSecret, final String loginRedirectUrl,
                           final String workspaceRedirectUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.loginRedirectUrl = loginRedirectUrl;
        this.workspaceRedirectUrl = workspaceRedirectUrl;
    }
}
