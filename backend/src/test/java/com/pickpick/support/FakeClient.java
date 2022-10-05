package com.pickpick.support;

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
    public String callAccessToken(final String code) {
        return code;
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
                .map(ChannelFixture::create)
                .orElseThrow(() -> new SlackApiCallException("test-callChannel"));
    }

    @Override
    public List<Member> findAllWorkspaceMembers() {
        return new ArrayList<>();
    }

    @Override
    public void sendMessage(final Reminder reminder) {

    }
}
