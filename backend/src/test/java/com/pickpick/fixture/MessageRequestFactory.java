package com.pickpick.fixture;

import com.pickpick.channel.domain.Channel;
import com.pickpick.message.domain.Message;
import com.pickpick.message.ui.dto.MessageRequest;
import java.util.List;
import java.util.stream.Collectors;

public class MessageRequestFactory {

    public static MessageRequest emptyQueryParams() {
        return new MessageRequest("", "", null, true, null, null);
    }

    public static MessageRequest onlyCount(final int limit) {
        return new MessageRequest("", "", null, true, null, limit);
    }

    public static MessageRequest fromLatestInChannels(final List<Channel> channels, final int limit) {
        return new MessageRequest("", "", extractChannelIds(channels), true, null, limit);
    }

    public static MessageRequest searchByKeywordInChannels(final List<Channel> channels, final String keyword,
                                                           final int limit) {
        return new MessageRequest(keyword, "", extractChannelIds(channels), true, null, limit);
    }

    public static MessageRequest pastFromTargetMessageInChannels(final List<Channel> channels, final Message message,
                                                                 final int limit) {
        return new MessageRequest("", "", extractChannelIds(channels), true, message.getId(), limit);
    }

    public static MessageRequest futureFromTargetMessageInChannels(final List<Channel> channels, final Message message,
                                                                   final int limit) {
        return new MessageRequest("", "", extractChannelIds(channels), false, message.getId(), limit);
    }

    private static List<Long> extractChannelIds(final List<Channel> channels) {
        return channels.stream()
                .map(Channel::getId).
                collect(Collectors.toList());
    }
}
