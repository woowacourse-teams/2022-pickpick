package com.pickpick.support;

import com.pickpick.fixture.FakeClientFixture;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Primary
    @Bean
    public ExternalClient externalClient(FakeClientFixture fakeClientFixture) {
        return new FakeClient(fakeClientFixture);
    }
}
