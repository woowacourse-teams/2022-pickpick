package com.pickpick.fixture;

import com.pickpick.channel.domain.Channel;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ChannelFixture {

    NOTICE("C0000000001", "공지사항"),
    FREE_CHAT("C0000000002", "잡담"),
    QNA("C0000000003", "질문답변"),
    ;

    private final String slackId;
    private final String name;

    ChannelFixture(final String slackId, final String name) {
        this.slackId = slackId;
        this.name = name;
    }

    public static List<Channel> createAllChannels() {
        return Arrays.stream(ChannelFixture.values())
                .map(ChannelFixture::create)
                .collect(Collectors.toList());
    }

    public Channel create() {
        return new Channel(slackId, name);
    }

    public boolean sameSlackId(final String slackId) {
        return this.slackId.equals(slackId);
    }
}
