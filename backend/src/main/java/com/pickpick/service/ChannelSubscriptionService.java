package com.pickpick.service;

import com.pickpick.controller.dto.ChannelResponse;
import com.pickpick.entity.Channel;
import com.pickpick.entity.ChannelSubscription;
import com.pickpick.entity.Member;
import com.pickpick.repository.ChannelRepository;
import com.pickpick.repository.ChannelSubscriptionRepository;
import com.pickpick.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChannelSubscriptionService {

    private final ChannelSubscriptionRepository channelSubscriptions;
    private final ChannelRepository channels;
    private final MemberRepository members;

    public ChannelSubscriptionService(ChannelSubscriptionRepository channelSubscriptions,
                                      ChannelRepository channels,
                                      MemberRepository members) {
        this.channelSubscriptions = channelSubscriptions;
        this.channels = channels;
        this.members = members;
    }

    public List<ChannelResponse> findAll(final Long memberId) {
        List<Channel> allChannels = channels.findAllByOrderByName();
        List<Channel> subscribedChannels = findSubscribedChannels(memberId);

        return getChannelResponsesWithIsSubscribed(allChannels, subscribedChannels);
    }

    public List<ChannelSubscription> findAllOrderByViewOrder(Long memberId) {
        return channelSubscriptions.findAllByMemberIdOrderByViewOrder(memberId);
    }

    private List<ChannelResponse> getChannelResponsesWithIsSubscribed(final List<Channel> allChannels,
                                                                      final List<Channel> subscribedChannels) {
        return allChannels.stream()
                .map(channel -> new ChannelResponse(channel.getId(), channel.getName(),
                        subscribedChannels.contains(channel)))
                .collect(Collectors.toList());
    }

    private List<Channel> findSubscribedChannels(final Long memberId) {
        return channelSubscriptions.findAllByMemberId(memberId)
                .stream()
                .map(ChannelSubscription::getChannel)
                .collect(Collectors.toList());
    }

    public void saveAll(final List<Channel> channels, final Long memberId) {
        Member member = members.findById(memberId);

        channelSubscriptions.deleteAllByMemberId(memberId);

        channelSubscriptions.saveAll(getChannelSubscriptionsByChannel(channels, member));
    }

    private List<ChannelSubscription> getChannelSubscriptionsByChannel(final List<Channel> channels,
                                                                       final Member member) {
        List<ChannelSubscription> subscriptions = new ArrayList<>();

        for (int i = 0; i < channels.size(); i++) {
            subscriptions.add(new ChannelSubscription(channels.get(i), member, i));
        }
        return subscriptions;
    }

}
