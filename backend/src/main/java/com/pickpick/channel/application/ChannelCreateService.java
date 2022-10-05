package com.pickpick.channel.application;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.support.ExternalClient;
import com.pickpick.workspace.domain.Workspace;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ChannelCreateService {

    private final ChannelRepository channels;
    private final ExternalClient slackClient;

    public ChannelCreateService(final ChannelRepository channels, final ExternalClient slackClient) {
        this.channels = channels;
        this.slackClient = slackClient;
    }

    public Channel createChannel(final String channelSlackId, final Workspace workspace) {
        Channel channel = slackClient.callChannel(channelSlackId, workspace);
        return channels.save(channel);
    }
}
