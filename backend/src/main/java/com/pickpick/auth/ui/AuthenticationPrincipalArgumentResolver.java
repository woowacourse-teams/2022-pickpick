package com.pickpick.auth.ui;

import com.pickpick.auth.support.AuthenticationPrincipal;
import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.utils.AuthorizationExtractor;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = AuthorizationExtractor.extract(toHttpServletRequest(webRequest));
        return Long.valueOf(jwtTokenProvider.getPayload(token));
    }

    private HttpServletRequest toHttpServletRequest(NativeWebRequest webRequest) {
        return webRequest.getNativeRequest(HttpServletRequest.class);
    }
}

