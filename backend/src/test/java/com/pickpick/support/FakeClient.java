package com.pickpick.support;

import com.pickpick.auth.application.dto.BotInfoDto;
import com.pickpick.channel.domain.Channel;
import com.pickpick.exception.SlackApiCallException;
import com.pickpick.fixture.ChannelFixture;
import com.pickpick.fixture.MemberFixture;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Reminder;
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
        return new BotInfoDto(code, code);
    }

    @Override
    public String callMemberSlackId(final String accessToken) {
        return accessToken;
    }

    @Override
    public Channel callChannel(final String channelSlackId, final Workspace workspace) {
        return Arrays.stream(ChannelFixture.values())
                .filter(it -> it.isSameSlackId(channelSlackId))
                .findAny()
                .map(channel -> channel.create(workspace))
                .orElseThrow(() -> new SlackApiCallException("test-callChannel"));
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
    public Map<String, Boolean> findParticipation(final String userToken) {
        return Map.of();
    }

    @Override
    public void sendMessage(final Reminder reminder) {

    }
}
