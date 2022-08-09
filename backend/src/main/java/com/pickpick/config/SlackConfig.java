package com.pickpick.config;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfig {

    private final SlackProperties slackProperties;

    public SlackConfig(final SlackProperties slackProperties) {
        this.slackProperties = slackProperties;
    }

    @Bean
    public MethodsClient methodsClient() {
        String botToken = slackProperties.getBotToken();

        return Slack.getInstance().methods(botToken);
    }
}
