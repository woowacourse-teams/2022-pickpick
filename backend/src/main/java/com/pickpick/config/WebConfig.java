package com.pickpick.config;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public MethodsClient slackClient() {
        return Slack.getInstance().methods();
    }
}
