package com.pickpick.support;

import com.pickpick.auth.application.dto.BotInfoDto;
import com.pickpick.channel.domain.Channel;
import com.pickpick.fixture.ChannelFixture;
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

    @Override
    public String callUserToken(final String code) {
        return code;
    }

    @Override
    public BotInfoDto callBotInfo(final String code) {
        return new BotInfoDto(code, code, code);
    }

    @Override
    public String callMemberSlackId(final String accessToken) {
        return accessToken;
    }

    @Override
    public List<Member> findAllWorkspaceMembers(final Workspace workspace) {
        return Arrays.stream(MemberFixture.values())
                .map(it -> it.create(workspace))
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> findChannelsByWorkspace(final Workspace workspace) {
        return Arrays.stream(ChannelFixture.values())
                .map(channel -> channel.create(workspace))
                .collect(Collectors.toList());
    }

    @Override
    public Participation findParticipation(final String userToken) {
        Map<String, Boolean> participation = Arrays.stream(ChannelFixture.values())
                .collect(Collectors.toMap(ChannelFixture::getSlackId, it -> true));

        return new Participation(participation);
    }

    @Override
    public void sendMessage(final Reminder reminder) {

    }

    @Override
    public void inviteBotToChannel(final Member member, final Channel channel) {

    }
}
