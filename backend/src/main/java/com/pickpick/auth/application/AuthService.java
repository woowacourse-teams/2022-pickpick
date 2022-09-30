package com.pickpick.auth.application;

import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.auth.ui.dto.LoginResponse;
import com.pickpick.exception.member.MemberNotFoundException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.support.SlackClient;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final MemberRepository members;
    private final SlackClient slackClient;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberRepository members, final SlackClient slackClient,
                       final JwtTokenProvider jwtTokenProvider) {
        this.members = members;
        this.slackClient = slackClient;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void verifyToken(final String token) {
        jwtTokenProvider.validateToken(token);
    }

    @Transactional
    public LoginResponse login(final String code) {
        String token = slackClient.findAccessToken(code);
        String memberSlackId = slackClient.findMemberSlackId(token);

        Member member = members.findBySlackId(memberSlackId)
                .orElseThrow(() -> new MemberNotFoundException(memberSlackId));

        boolean isFirstLogin = member.isFirstLogin();
        member.markLoggedIn();

        return LoginResponse.builder()
                .token(jwtTokenProvider.createToken(String.valueOf(member.getId())))
                .firstLogin(isFirstLogin)
                .build();
    }
}
