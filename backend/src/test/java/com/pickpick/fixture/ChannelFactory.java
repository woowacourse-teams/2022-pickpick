package com.pickpick.fixture;

import com.pickpick.channel.domain.Channel;

public class ChannelFactory {

    private static final String 공지사항_slackId = "C00001";
    private static final String 공지사항_이름 = "공지사항";

    private static final String 잡담_slackId = "C00002";
    private static final String 잡담_이름 = "4기 잡담";

    public static Channel 공지사항() {
        return new Channel(공지사항_slackId, 공지사항_이름);
    }

    public static Channel 잡담() {
        return new Channel(잡담_slackId, 잡담_이름);
    }

    public static Channel channel(final String slackId, final String name) {
        return new Channel(slackId, name);
    }
}
