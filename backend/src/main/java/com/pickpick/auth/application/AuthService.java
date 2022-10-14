package com.pickpick.auth.application;

import com.pickpick.auth.application.dto.BotInfoDto;
import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.auth.ui.dto.LoginResponse;
import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.support.ExternalClient;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository members;
    private final WorkspaceRepository workspaces;
    private final ChannelRepository channels;
    private final ExternalClient slackClient;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberRepository members, final WorkspaceRepository workspaces,
                       final ChannelRepository channels, final ExternalClient slackClient,
                       final JwtTokenProvider jwtTokenProvider) {
        this.members = members;
        this.workspaces = workspaces;
        this.channels = channels;
        this.slackClient = slackClient;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void verifyToken(final String token) {
        jwtTokenProvider.validateToken(token);
    }

    @Transactional
    public LoginResponse login(final String code) {
        String userToken = slackClient.callUserToken(code);
        String memberSlackId = slackClient.callMemberSlackId(userToken);

        Member member = members.getBySlackId(memberSlackId);

        boolean isFirstLogin = member.isFirstLogin();
        member.firstLogin(userToken);

        return LoginResponse.builder()
                .token(jwtTokenProvider.createToken(String.valueOf(member.getId())))
                .firstLogin(isFirstLogin)
                .build();
    }

    @Transactional
    public LoginResponse registerWorkspace(final String code) {
        BotInfoDto botInfoDto = slackClient.callBotInfo(code);
        Workspace workspace = workspaces.save(botInfoDto.toEntity());

        List<Member> allWorkspaceMembers = slackClient.findMembersByWorkspace(workspace);
        members.saveAll(allWorkspaceMembers);

        List<Channel> allWorkspaceChannels = slackClient.findChannelsByWorkspace(workspace);
        channels.saveAll(allWorkspaceChannels);

        return login(code);
    }
}
