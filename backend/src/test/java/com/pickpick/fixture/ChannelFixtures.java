package com.pickpick.fixture;

import com.pickpick.channel.domain.Channel;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public enum ChannelFixtures {

    백엔드_잡담("백엔드-잡담", "C03NNNFNY01"),
    팀_공지("팀-공지", "C03N6B801S7"),
    test_4기_잡담("test-4기-잡담", "C03MEKY23L7"),
    test_4기_공지사항("test-4기-공지사항", "C03N7SSTUMP"),
    test_be_4기_공지("test-be-4기-공지", "C03MV690W2X"),
    test_fe_4기_공지("test-fe-4기-공지", "C03MEKQJBAB"),
    ;

    private final String name;
    private final String slackId;

    ChannelFixtures(final String name, final String slackId) {
        this.name = name;
        this.slackId = slackId;
    }

    public static List<Channel> allChannels() {
        return Arrays.stream(ChannelFixtures.values())
                .map(ChannelFixtures::toChannel)
                .collect(Collectors.toList());
    }

    public Channel toChannel() {
        return new Channel(slackId, name);
    }
}
