package com.pickpick.support;

import com.pickpick.fixture.StubSlack;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Primary
    @Bean
    public ExternalClient externalClient(StubSlack stubSlack) {
        return new FakeClient(stubSlack);
    }
}
