package com.pickpick.auth.application;

import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.auth.ui.dto.LoginResponse;
import com.pickpick.exception.MemberNotFoundException;
import com.pickpick.exception.SlackClientException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.oauth.OAuthV2AccessRequest;
import com.slack.api.methods.request.users.UsersIdentityRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUrl;

    private final MemberRepository members;
    private final MethodsClient slackClient;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(@Value("${slack.client-id}") final String clientId,
                       @Value("${slack.client-secret}") final String clientSecret,
                       @Value("${slack.redirect-url}") final String redirectUrl,
                       final MemberRepository members,
                       final MethodsClient slackClient,
                       final JwtTokenProvider jwtTokenProvider) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUrl = redirectUrl;
        this.members = members;
        this.slackClient = slackClient;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(final String code) {
        try {
            String token = requestSlackToken(code);
            String memberSlackId = requestMemberSlackId(token);

            Member member = members.findBySlackId(memberSlackId)
                    .orElseThrow(() -> new MemberNotFoundException(memberSlackId));

            boolean isFirstLogin = member.isFirstLogin();
            member.markLoggedIn();

            return LoginResponse.builder()
                    .token(jwtTokenProvider.createToken(String.valueOf(member.getId())))
                    .firstLogin(isFirstLogin)
                    .build();
        } catch (IOException | SlackApiException e) {
            throw new SlackClientException(e);
        }
    }

    private String requestSlackToken(final String code) throws IOException, SlackApiException {
        OAuthV2AccessRequest request = OAuthV2AccessRequest.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUrl)
                .code(code)
                .build();

        return slackClient.oauthV2Access(request)
                .getAuthedUser()
                .getAccessToken();
    }

    private String requestMemberSlackId(final String token) throws IOException, SlackApiException {
        UsersIdentityRequest request = UsersIdentityRequest.builder()
                .token(token)
                .build();

        return slackClient.usersIdentity(request)
                .getUser()
                .getId();
    }

    public void verifyToken(final String token) {
        jwtTokenProvider.validateToken(token);
    }
}
