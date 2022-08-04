package com.pickpick.config;

import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.auth.ui.AuthenticationInterceptor;
import com.pickpick.auth.ui.AuthenticationPrincipalArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalConfig(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(jwtTokenProvider));
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(jwtTokenProvider))
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/event", "/api/slack-login", "/api/certification");
    }
}
