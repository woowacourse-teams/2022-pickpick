package com.pickpick.channel.application;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.support.SlackClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ChannelCreateService {

    private final ChannelRepository channels;
    private final SlackClient slackClient;

    public ChannelCreateService(final ChannelRepository channels, final SlackClient slackClient) {
        this.channels = channels;
        this.slackClient = slackClient;
    }

    public Channel createChannel(final String channelSlackId) {
        Channel channel = slackClient.findChannel(channelSlackId);
        return channels.save(channel);
    }
}
