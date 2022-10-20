package com.pickpick.auth.application;

import com.pickpick.auth.application.dto.WorkspaceInfoDto;
import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.auth.ui.dto.LoginResponse;
import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.workspace.WorkspaceDuplicateException;
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
    private final ExternalClient externalClient;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberRepository members, final WorkspaceRepository workspaces,
                       final ChannelRepository channels, final ExternalClient externalClient,
                       final JwtTokenProvider jwtTokenProvider) {
        this.members = members;
        this.workspaces = workspaces;
        this.channels = channels;
        this.externalClient = externalClient;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void verifyToken(final String token) {
        jwtTokenProvider.validateToken(token);
    }

    @Transactional
    public LoginResponse registerWorkspace(final String code) {
        WorkspaceInfoDto workspaceInfoDto = externalClient.callWorkspaceInfo(code);

        validateExistWorkspace(workspaceInfoDto.getWorkspaceSlackId());

        Workspace workspace = workspaces.save(workspaceInfoDto.toEntity());

        List<Member> allWorkspaceMembers = externalClient.findMembersByWorkspace(workspace);
        members.saveAll(allWorkspaceMembers);

        List<Channel> allWorkspaceChannels = externalClient.findChannelsByWorkspace(workspace);
        channels.saveAll(allWorkspaceChannels);

        return loginByToken(workspaceInfoDto.getUserToken());
    }

    private void validateExistWorkspace(final String workspaceSlackId) {
        if (workspaces.existsBySlackId(workspaceSlackId)) {
            throw new WorkspaceDuplicateException(workspaceSlackId);
        }
    }

    @Transactional
    public LoginResponse login(final String code) {
        String userToken = externalClient.callUserToken(code);
        return loginByToken(userToken);
    }

    private LoginResponse loginByToken(final String userSlackToken) {
        String memberSlackId = externalClient.callMemberSlackId(userSlackToken);

        Member member = members.getBySlackId(memberSlackId);

        boolean isFirstLogin = member.isFirstLogin();
        member.firstLogin(userSlackToken);

        return LoginResponse.builder()
                .token(jwtTokenProvider.createToken(String.valueOf(member.getId())))
                .firstLogin(isFirstLogin)
                .build();
    }
}
