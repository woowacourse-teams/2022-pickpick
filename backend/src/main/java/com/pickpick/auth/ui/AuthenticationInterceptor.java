package com.pickpick.auth.ui;

import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.utils.AuthorizationExtractor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (CorsUtils.isCorsRequest(request)) {
            return true;
        }

        String token = AuthorizationExtractor.extract(request);
        jwtTokenProvider.validateToken(token);
        return true;
    }
}
