package com.pickpick.fixture;

import com.pickpick.channel.domain.Channel;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ChannelFixture {

    TEST_BE_4_NOTICE("C03NNNFNY01", "test-be-4기-공지"),
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

    public static List<Channel> allChannels() {
        return Arrays.stream(ChannelFixture.values())
                .map(ChannelFixture::create)
                .collect(Collectors.toList());
    }

    public Channel create() {
        return new Channel(slackId, name);
    }
}
