package com.pickpick.fixture;

import com.pickpick.channel.domain.Channel;
import com.pickpick.message.domain.Message;
import com.pickpick.message.ui.dto.MessageRequest;
import java.util.List;
import java.util.stream.Collectors;

public class MessageRequestFactory {

    public static MessageRequest 쿼리_파라미터가_없는_요청(final int limit) {
        return new MessageRequest("", "", null, true, null, limit);
    }

    public static MessageRequest 여러채널의_최신부터_내림차순_요청(final List<Channel> channels, final int limit) {
        return new MessageRequest("", "", extractChannelIds(channels), true, null, limit);
    }

    public static MessageRequest 검색대상_여러채널과_키워드로_요청(final List<Channel> channels, final String keyword,
                                                    final int limit) {
        return new MessageRequest(keyword, "", extractChannelIds(channels), true, null, limit);
    }

    public static MessageRequest 여러채널에서_기준_메시지의_과거_내림차순_요청(final List<Channel> channels, final Message message,
                                                           final int limit) {
        return new MessageRequest("", "", extractChannelIds(channels), true, message.getId(), limit);
    }

    public static MessageRequest 여러채널에서_기준_메시지의_미래_오름차순_요청(final List<Channel> channels, final Message message,
                                                           final int limit) {
        return new MessageRequest("", "", extractChannelIds(channels), false, message.getId(), limit);
    }

    private static List<Long> extractChannelIds(final List<Channel> channels) {
        return channels.stream()
                .map(Channel::getId).
                collect(Collectors.toList());
    }
}
