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
    private final String botToken;

    @NotBlank
    private final String clientId;

    @NotBlank
    private final String clientSecret;

    @NotBlank
    private final String redirectUrl;

    public SlackProperties(final String botToken, final String clientId, final String clientSecret,
                           final String redirectUrl) {
        this.botToken = botToken;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUrl = redirectUrl;
    }
}
