package com.pickpick.fixture;

import static com.pickpick.fixture.ChannelFixture.FREE_CHAT;
import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.ChannelFixture.QNA;
import static com.pickpick.fixture.MemberFixture.SUMMER;
import static com.pickpick.fixture.MemberFixture.YEONLOG;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;

import com.pickpick.channel.domain.Channel;
import com.pickpick.member.domain.Member;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ParticipationFixture {
    YEONLOG_IN_ALL_CHANNELS(YEONLOG.getSlackId(), List.of(NOTICE, FREE_CHAT, QNA)),
    SUMMER_IN_ONE_CHANNEL(SUMMER.getSlackId(), List.of(NOTICE)),
    ;

    private final String memberSlackId;
    private final List<ChannelFixture> channels;

    ParticipationFixture(final String memberSlackId, final List<ChannelFixture> channels) {
        this.memberSlackId = memberSlackId;
        this.channels = channels;
    }

    public static List<Channel> findParticipation(final Member member) {
        return Arrays.stream(values())
                .filter(fixture -> fixture.memberSlackId.equals(member.getSlackId()))
                .flatMap(fixture -> fixture.channels.stream())
                .map(channelFixture -> channelFixture.create(JUPJUP.create()))
                .collect(Collectors.toList());
    }
}
