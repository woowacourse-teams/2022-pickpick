package com.pickpick.support;

import static com.pickpick.fixture.FakeClientFixture.codeAndMember;
import static com.pickpick.fixture.FakeClientFixture.codeAndWorkspace;
import static com.pickpick.fixture.FakeClientFixture.tokenAndMember;

import com.pickpick.auth.application.dto.WorkspaceInfoDto;
import com.pickpick.channel.domain.Channel;
import com.pickpick.fixture.ChannelFixture;
import com.pickpick.fixture.FakeClientFixture;
import com.pickpick.fixture.MemberFixture;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Reminder;
import com.pickpick.slackevent.domain.Participation;
import com.pickpick.workspace.domain.Workspace;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeClient implements ExternalClient {

    private final FakeClientFixture fakeClientFixture;

    public FakeClient(final FakeClientFixture fakeClientFixture) {
        this.fakeClientFixture = fakeClientFixture;
    }

    @Override
    public String callUserToken(final String code) {
        return codeAndMember.get(code).getToken();
    }

    @Override
    public WorkspaceInfoDto callWorkspaceInfo(final String code) {
        Workspace workspace = codeAndWorkspace.get(code);
        return new WorkspaceInfoDto(workspace.getSlackId(), workspace.getBotToken(), workspace.getBotSlackId());
    }

    @Override
    public String callMemberSlackId(final String userToken) {
        return tokenAndMember.get(userToken).getSlackId();
    }

    @Override
    public List<Member> findMembersByWorkspace(final Workspace workspace) {
        return Arrays.stream(MemberFixture.values())
                .map(memberFixture -> memberFixture.createNeverLoggedIn(workspace))
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> findChannelsByWorkspace(final Workspace workspace) {
        return Arrays.stream(ChannelFixture.values())
                .filter(ChannelFixture::isJupjupChannel)
                .map(channelFixture -> channelFixture.create(workspace))
                .collect(Collectors.toList());
//
//        return Arrays.stream(ChannelFixture.values())
//                .filter(ChannelFixture::isDefaultChannel)
//                .map(channel -> channel.create(workspace))
//                .collect(Collectors.toList());
    }

    @Override
    public Participation findChannelParticipation(final String userToken) {
        Member member = tokenAndMember.get(userToken);
        List<Channel> participatingChannels = fakeClientFixture.getMemberParticipatingChannels().get(member.getToken());

        Map<String, Boolean> participation = Arrays.stream(ChannelFixture.values())
                .collect(Collectors.toMap(ChannelFixture::getSlackId,
                        fixture -> isParticipatingChannel(fixture, participatingChannels)));

        return new Participation(participation);

//        Map<String, Boolean> participation = Arrays.stream(ChannelFixture.values())
//                .collect(Collectors.toMap(ChannelFixture::getSlackId, it -> true));
//
//        return new Participation(participation);
    }

    @Override
    public void sendMessage(final Reminder reminder) {

    }

    @Override
    public void inviteBotToChannel(final Member member, final Channel channel) {

    }

    private boolean isParticipatingChannel(ChannelFixture fixture, List<Channel> participatingChannels) {
        return participatingChannels.stream()
                .map(Channel::getSlackId)
                .anyMatch(it -> it.equals(fixture.getSlackId()));
    }
}
