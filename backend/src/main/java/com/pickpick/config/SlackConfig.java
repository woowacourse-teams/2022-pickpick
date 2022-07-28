package com.pickpick.config;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfig {

    @Bean
    public MethodsClient methodsClient() {
        return Slack.getInstance().methods();
    }
}
