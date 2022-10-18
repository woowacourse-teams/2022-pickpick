package com.pickpick.support;


import com.pickpick.auth.application.dto.WorkspaceInfoDto;
import com.pickpick.channel.domain.Channel;
import com.pickpick.fixture.ChannelFixture;
import com.pickpick.fixture.MemberFixture;
import com.pickpick.fixture.StubSlack;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Reminder;
import com.pickpick.slackevent.domain.Participation;
import com.pickpick.workspace.domain.Workspace;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeClient implements ExternalClient {

    private final StubSlack stubSlack;

    public FakeClient(final StubSlack stubSlack) {
        this.stubSlack = stubSlack;
    }

    @Override
    public String callUserToken(final String code) {
        return stubSlack.callUserToken(code);
    }

    @Override
    public WorkspaceInfoDto callWorkspaceInfo(final String code) {
        return stubSlack.callWorkspaceInfo(code);
    }

    @Override
    public String callMemberSlackId(final String userToken) {
        return stubSlack.callMemberSlackId(userToken);
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
    }

    @Override
    public Participation findChannelParticipation(final String userToken) {
        List<Channel> participatingChannels = stubSlack.getParticipatingChannels(userToken);
        Map<String, Boolean> participation = createParticipation(participatingChannels);

        return new Participation(participation);
    }

    @Override
    public void sendMessage(final Reminder reminder) {

    }

    @Override
    public void inviteBotToChannel(final Member member, final Channel channel) {

    }

    private Map<String, Boolean> createParticipation(final List<Channel> participatingChannels) {
        return Arrays.stream(ChannelFixture.values())
                .collect(Collectors.toMap(ChannelFixture::getSlackId,
                        fixture -> isParticipatingChannel(fixture, participatingChannels)));
    }

    private boolean isParticipatingChannel(ChannelFixture fixture, List<Channel> participatingChannels) {
        return participatingChannels.stream()
                .map(Channel::getSlackId)
                .anyMatch(it -> it.equals(fixture.getSlackId()));
    }
}
