package com.pickpick.service;

import com.pickpick.entity.Channel;
import com.pickpick.entity.ChannelSubscription;
import com.pickpick.entity.Member;
import com.pickpick.repository.ChannelSubscriptionRepository;
import com.pickpick.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChannelSubscriptionService {

    private final ChannelSubscriptionRepository channelSubscriptions;
    private final MemberRepository members;

    public ChannelSubscriptionService(ChannelSubscriptionRepository channelSubscriptions,
                                      MemberRepository members) {
        this.channelSubscriptions = channelSubscriptions;
        this.members = members;
    }

    public List<ChannelSubscription> findAll(Long memberId) {
        return channelSubscriptions.findAllByMemberIdOrderByChannelName(memberId);
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
