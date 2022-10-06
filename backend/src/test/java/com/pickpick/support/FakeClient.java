package com.pickpick.support;

import com.pickpick.auth.application.dto.BotInfoDto;
import com.pickpick.channel.domain.Channel;
import com.pickpick.exception.SlackApiCallException;
import com.pickpick.fixture.ChannelFixture;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Reminder;
import com.pickpick.workspace.domain.Workspace;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeClient implements ExternalClient {

    @Override
    public String callUserToken(final String code) {
        return code;
    }

    @Override
    public BotInfoDto callBotInfo(final String code) {
        return null;
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
        return new ArrayList<>();
    }

    @Override
    public void sendMessage(final Reminder reminder) {

    }

    @Override
    public List<Channel> findAllWorkspaceChannels(final Workspace workspace) {
        return new ArrayList<>();
    }
}
