package com.pickpick.channel.application;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.SlackApiCallException;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Conversation;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class ChannelService {

    private final ChannelRepository channels;
    private final MethodsClient slackClient;

    public ChannelService(final ChannelRepository channels, final MethodsClient slackClient) {
        this.channels = channels;
        this.slackClient = slackClient;
    }

    public Channel createChannel(final String channelSlackId) {
        try {
            Conversation conversation = slackClient.conversationsInfo(
                    request -> request.channel(channelSlackId)
            ).getChannel();

            Channel channel = toChannel(conversation);

            return channels.save(channel);
        } catch (IOException | SlackApiException e) {
            throw new SlackApiCallException(e);
        }
    }

    private Channel toChannel(final Conversation channel) {
        return new Channel(channel.getId(), channel.getName());
    }
}
