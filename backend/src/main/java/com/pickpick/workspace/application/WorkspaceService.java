package com.pickpick.workspace.application;

import com.pickpick.auth.application.dto.OAuthAccessInfoDto;
import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.workspace.WorkspaceDuplicateException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.support.ExternalClient;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaces;
    private final ChannelRepository channels;
    private final MemberRepository members;
    private final ExternalClient externalClient;

    public WorkspaceService(final WorkspaceRepository workspaces, final ChannelRepository channels,
                            final MemberRepository members, final ExternalClient externalClient) {
        this.workspaces = workspaces;
        this.channels = channels;
        this.members = members;
        this.externalClient = externalClient;
    }

    @Transactional
    public OAuthAccessInfoDto register(final String code) {
        OAuthAccessInfoDto oAuthAccessInfoDto = externalClient.callOAuthAccessInfo(code);
        validateUnregisteredWorkspace(oAuthAccessInfoDto.getWorkspaceSlackId());

        initWorkspaceInfos(oAuthAccessInfoDto);

        return oAuthAccessInfoDto;
    }

    private void initWorkspaceInfos(final OAuthAccessInfoDto workspaceInfoDto) {
        Workspace workspace = workspaces.save(workspaceInfoDto.toWorkspace());

        List<Member> allWorkspaceMembers = externalClient.findMembersByWorkspace(workspace);
        members.saveAll(allWorkspaceMembers);

        List<Channel> allWorkspaceChannels = externalClient.findChannelsByWorkspace(workspace);
        channels.saveAll(allWorkspaceChannels);
    }

    private void validateUnregisteredWorkspace(final String workspaceSlackId) {
        if (workspaces.existsBySlackId(workspaceSlackId)) {
            throw new WorkspaceDuplicateException(workspaceSlackId);
        }
    }
}
