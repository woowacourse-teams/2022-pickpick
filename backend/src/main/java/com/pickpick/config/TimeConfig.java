package com.pickpick.config;

import java.time.Clock;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {

    @PostConstruct
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
