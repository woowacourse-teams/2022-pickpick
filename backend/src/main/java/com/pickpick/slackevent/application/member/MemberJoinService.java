package com.pickpick.slackevent.application.member;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.member.dto.MemberJoinDto;
import com.pickpick.slackevent.application.member.dto.MemberRequest;
import com.pickpick.utils.JsonUtils;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberJoinService implements SlackEventService {

    private final MemberRepository members;
    private final WorkspaceRepository workspaces;

    public MemberJoinService(final MemberRepository members, final WorkspaceRepository workspaces) {
        this.members = members;
        this.workspaces = workspaces;
    }

    @Override
    public void execute(final String requestBody) {
        MemberJoinDto memberJoinDto = convert(requestBody);
        Workspace workspace = workspaces.getBySlackId(memberJoinDto.getWorkspaceSlackId());
        Member newMember = memberJoinDto.toEntity(workspace);

        members.save(newMember);
    }

    private MemberJoinDto convert(final String requestBody) {
        MemberRequest request = JsonUtils.convert(requestBody, MemberRequest.class);
        return request.toMemberJoinDto();
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MEMBER_JOIN == slackEvent;
    }
}
