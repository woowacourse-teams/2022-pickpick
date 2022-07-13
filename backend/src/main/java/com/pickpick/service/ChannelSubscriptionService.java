package com.pickpick.service;

import com.pickpick.entity.Channel;
import com.pickpick.entity.ChannelSubscription;
import com.pickpick.repository.ChannelSubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChannelSubscriptionService {

    private final ChannelSubscriptionRepository channelSubscriptions;

    public ChannelSubscriptionService(ChannelSubscriptionRepository channelSubscriptions) {
        this.channelSubscriptions = channelSubscriptions;
    }

    public void saveAll(final List<Channel> channels) {
        channelSubscriptions.deleteAll();

        channelSubscriptions.saveAll(getChannelSubscriptionsByChannel(channels));
    }

    private List<ChannelSubscription> getChannelSubscriptionsByChannel(final List<Channel> channels) {
        List<ChannelSubscription> subscriptions = new ArrayList<>();

        for (int i = 0; i < channels.size(); i++) {
            subscriptions.add(new ChannelSubscription(channels.get(i), i));
        }
        return subscriptions;
    }

    public List<ChannelSubscription> findAll() {
        return channelSubscriptions.findAllByOrderByViewOrder();
    }
}
