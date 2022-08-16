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

    public static MessageRequest emptyQueryParamsWithCount(final int limit) {
        return new MessageRequest("", "", null, true, null, limit);
    }

    public static MessageRequest fromLatestInChannelIds(final List<Long> channelIds, final int limit) {
        return new MessageRequest("", "", channelIds, true, null, limit);
    }

    public static MessageRequest fromLatestInChannels(final List<Channel> channels, final int limit) {
        return new MessageRequest("", "", extractChannelIds(channels), true, null, limit);
    }

    public static MessageRequest searchByKeywordInChannels(final List<Channel> channels, final String keyword,
                                                           final int limit) {
        return new MessageRequest(keyword, "", extractChannelIds(channels), true, null, limit);
    }

    public static MessageRequest searchByKeywordInChannelIds(final List<Long> channelIds, final String keyword,
                                                             final int limit) {
        return new MessageRequest(keyword, "", channelIds, true, null, limit);
    }

    public static MessageRequest pastFromTargetMessageInChannels(final List<Channel> channels, final Message message,
                                                                 final int limit) {
        return new MessageRequest("", "", extractChannelIds(channels), true, message.getId(), limit);
    }

    public static MessageRequest pastFromTargetMessageInChannelIds(final List<Long> channelIds, final Long messageId,
                                                                   final int limit) {
        return new MessageRequest("", "", channelIds, true, messageId, limit);
    }

    public static MessageRequest futureFromTargetMessageInChannels(final List<Channel> channels, final Message message,
                                                                   final int limit) {
        return new MessageRequest("", "", extractChannelIds(channels), false, message.getId(), limit);
    }

    public static MessageRequest futureFromTargetMessageInChannelIds(final List<Long> channelIds, final Long messageId,
                                                                   final int limit) {
        return new MessageRequest("", "", channelIds, false, messageId, limit);
    }

    private static List<Long> extractChannelIds(final List<Channel> channels) {
        return channels.stream()
                .map(Channel::getId).
                collect(Collectors.toList());
    }
}
