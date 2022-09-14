package com.pickpick.channel.application;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.channel.domain.ChannelSubscription;
import com.pickpick.channel.domain.ChannelSubscriptionRepository;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.channel.ui.dto.ChannelResponses;
import com.pickpick.exception.SlackApiCallException;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Conversation;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ChannelService {

    private final ChannelRepository channels;
    private final ChannelSubscriptionRepository channelSubscriptions;
    private final MethodsClient slackClient;

    public ChannelService(final ChannelRepository channels, final ChannelSubscriptionRepository channelSubscriptions,
                          final MethodsClient slackClient) {
        this.channels = channels;
        this.channelSubscriptions = channelSubscriptions;
        this.slackClient = slackClient;
    }

    @Transactional
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

    public ChannelResponses findAll(final Long memberId) {
        List<Channel> allChannels = channels.findAllByOrderByName();
        Map<Long, Channel> subscribedChannels = findSubscribedChannels(memberId);

        List<ChannelResponse> channelResponses = findChannelResponses(allChannels, subscribedChannels);
        return new ChannelResponses(channelResponses);
    }


    private List<ChannelResponse> findChannelResponses(final List<Channel> allChannels,
                                                       final Map<Long, Channel> subscribedChannels) {
        return allChannels.stream()
                .map(channel -> ChannelResponse.of(subscribedChannels, channel))
                .collect(Collectors.toList());
    }

    private Map<Long, Channel> findSubscribedChannels(final Long memberId) {
        return channelSubscriptions.findAllByMemberId(memberId)
                .stream()
                .collect(Collectors.toMap(ChannelSubscription::getChannelId, ChannelSubscription::getChannel));
    }

}
