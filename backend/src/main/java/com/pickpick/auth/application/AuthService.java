package com.pickpick.auth.application;

import com.pickpick.auth.application.dto.MemberInfoDto;
import com.pickpick.auth.application.dto.OAuthAccessInfoDto;
import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.auth.ui.dto.LoginResponse;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.support.ExternalClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository members;
    private final ExternalClient externalClient;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberRepository members, final ExternalClient externalClient,
                       final JwtTokenProvider jwtTokenProvider) {
        this.members = members;
        this.externalClient = externalClient;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void verifyToken(final String token) {
        jwtTokenProvider.validateToken(token);
    }

    @Transactional
    public LoginResponse login(final String code) {
        MemberInfoDto memberInfoDto = externalClient.callMemberInfo(code);
        return login(memberInfoDto.getSlackId(), memberInfoDto.getUserToken());
    }

    @Transactional
    public LoginResponse login(final OAuthAccessInfoDto oAuthAccessInfoDto) {
        return login(oAuthAccessInfoDto.getUserSlackId(), oAuthAccessInfoDto.getUserToken());
    }

    private LoginResponse login(final String memberSlackId, final String memberToken) {
        Member member = members.getBySlackId(memberSlackId);

        boolean isFirstLogin = member.isFirstLogin();
        member.firstLogin(memberToken);

        return LoginResponse.builder()
                .token(jwtTokenProvider.createToken(String.valueOf(member.getId())))
                .firstLogin(isFirstLogin)
                .build();
    }
}
