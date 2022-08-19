package com.pickpick.auth.application;

import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.auth.ui.dto.LoginResponse;
import com.pickpick.config.SlackProperties;
import com.pickpick.exception.SlackApiCallException;
import com.pickpick.exception.member.MemberNotFoundException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.oauth.OAuthV2AccessRequest;
import com.slack.api.methods.request.users.UsersIdentityRequest;
import java.io.IOException;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final MemberRepository members;
    private final MethodsClient slackClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final SlackProperties slackProperties;

    public AuthService(final MemberRepository members, final MethodsClient slackClient,
                       final JwtTokenProvider jwtTokenProvider, final SlackProperties slackProperties) {
        this.members = members;
        this.slackClient = slackClient;
        this.jwtTokenProvider = jwtTokenProvider;
        this.slackProperties = slackProperties;
    }

    public void verifyToken(final String token) {
        jwtTokenProvider.validateToken(token);
    }

    @Transactional
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
            throw new SlackApiCallException(e);
        }
    }

    private String requestSlackToken(final String code) throws IOException, SlackApiException {
        OAuthV2AccessRequest request = OAuthV2AccessRequest.builder()
                .clientId(slackProperties.getClientId())
                .clientSecret(slackProperties.getClientSecret())
                .redirectUri(slackProperties.getRedirectUrl())
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
}
