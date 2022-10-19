package com.pickpick.fixture;

import com.pickpick.channel.domain.Channel;
import com.pickpick.workspace.domain.Workspace;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ChannelFixture {

    NOTICE("C0000000001", "공지사항", true),
    FREE_CHAT("C0000000002", "잡담", true),
    QNA("C0000000003", "질문답변", true),

    NEW_CHANNEL("C0000000004", "새로운 채널", false),
    ;

    private final String slackId;
    private final String name;
    private final boolean defaultChannel;

    ChannelFixture(final String slackId, final String name, final boolean defaultChannel) {
        this.slackId = slackId;
        this.name = name;
        this.defaultChannel = defaultChannel;
    }

    public static List<Channel> createAllChannels(final Workspace workspace) {
        return Arrays.stream(ChannelFixture.values())
                .map(channel -> channel.create(workspace))
                .collect(Collectors.toList());
    }

    public static int getDefaultSize() {
        return (int) Arrays.stream(ChannelFixture.values())
                .filter(ChannelFixture::isDefaultChannel)
                .count();
    }

    // TODO 제거 필요
    public Channel create() {
        return new Channel(slackId, name);
    }

    public Channel create(final Workspace workspace) {
        return new Channel(slackId, name, workspace);
    }

    public boolean isSameSlackId(final String slackId) {
        return this.slackId.equals(slackId);
    }

    public String getSlackId() {
        return slackId;
    }

    public boolean isDefaultChannel() {
        return defaultChannel;
    }
}
