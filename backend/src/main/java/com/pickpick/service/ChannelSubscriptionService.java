package com.pickpick.service;

import com.pickpick.controller.dto.ChannelOrderRequest;
import com.pickpick.controller.dto.ChannelResponse;
import com.pickpick.controller.dto.ChannelSubscriptionRequest;
import com.pickpick.entity.Channel;
import com.pickpick.entity.ChannelSubscription;
import com.pickpick.entity.Member;
import com.pickpick.exception.ChannelNotFoundException;
import com.pickpick.exception.MemberNotFoundException;
import com.pickpick.exception.SubscriptionDuplicatedException;
import com.pickpick.exception.SubscriptionNotExistException;
import com.pickpick.repository.ChannelRepository;
import com.pickpick.repository.ChannelSubscriptionRepository;
import com.pickpick.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ChannelSubscriptionService {

    private static final int ORDER_FIRST = 1;
    private static final int ORDER_NEXT = 1;

    private final ChannelSubscriptionRepository channelSubscriptions;
    private final ChannelRepository channels;
    private final MemberRepository members;

    public ChannelSubscriptionService(final ChannelSubscriptionRepository channelSubscriptions,
                                      final ChannelRepository channels,
                                      final MemberRepository members) {
        this.channelSubscriptions = channelSubscriptions;
        this.channels = channels;
        this.members = members;
    }

    public List<ChannelResponse> findAll(final Long memberId) {
        List<Channel> allChannels = channels.findAllByOrderByName();
        List<Channel> subscribedChannels = findSubscribedChannels(memberId);

        return getChannelResponsesWithIsSubscribed(allChannels, subscribedChannels);
    }

    public List<ChannelSubscription> findAllOrderByViewOrder(final Long memberId) {
        return channelSubscriptions.findAllByMemberIdOrderByViewOrder(memberId);
    }

    private List<ChannelResponse> getChannelResponsesWithIsSubscribed(final List<Channel> allChannels,
                                                                      final List<Channel> subscribedChannels) {
        return allChannels.stream()
                .map(channel -> ChannelResponse.of(subscribedChannels, channel))
                .collect(Collectors.toList());
    }

    private List<Channel> findSubscribedChannels(final Long memberId) {
        return channelSubscriptions.findAllByMemberId(memberId)
                .stream()
                .map(ChannelSubscription::getChannel)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveAll(final List<Channel> channels, final Long memberId) {
        Member member = members.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        channelSubscriptions.deleteAllByMemberId(memberId);

        channelSubscriptions.saveAll(getChannelSubscriptionsByChannel(channels, member));
    }

    @Transactional
    public void save(final ChannelSubscriptionRequest subscriptionRequest, final Long memberId) {
        Long channelId = subscriptionRequest.getId();
        Channel channel = channels.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));

        Member member = members.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        validateDuplicatedSubscription(channel, member);

        channelSubscriptions.save(new ChannelSubscription(channel, member, getMaxViewOrder(memberId)));
    }

    private void validateDuplicatedSubscription(final Channel channel, final Member member) {
        if (channelSubscriptions.existsByChannelAndMember(channel, member)) {
            throw new SubscriptionDuplicatedException(channel.getId());
        }
    }

    private int getMaxViewOrder(final Long memberId) {
        return channelSubscriptions.findFirstByMemberIdOrderByViewOrderDesc(memberId)
                .map(it -> it.getViewOrder() + ORDER_NEXT)
                .orElse(ORDER_FIRST);
    }

    private List<ChannelSubscription> getChannelSubscriptionsByChannel(final List<Channel> channels,
                                                                       final Member member) {
        List<ChannelSubscription> subscriptions = new ArrayList<>();

        for (int i = 0; i < channels.size(); i++) {
            subscriptions.add(new ChannelSubscription(channels.get(i), member, i));
        }
        return subscriptions;
    }

    @Transactional
    public void updateOrders(final List<ChannelOrderRequest> orderRequests, final Long memberId) {
        List<ChannelSubscription> subscribedChannels = channelSubscriptions.findAllByMemberId(memberId);
        Map<Long, Integer> ordersByChannelId = getOrdersMap(orderRequests);

        for (ChannelSubscription subscribedChannel : subscribedChannels) {
            subscribedChannel.changeOrder(ordersByChannelId.get(subscribedChannel.getChannelId()));
        }
    }

    private Map<Long, Integer> getOrdersMap(final List<ChannelOrderRequest> orderRequests) {
        return orderRequests.stream()
                .collect(Collectors.toMap(ChannelOrderRequest::getId, ChannelOrderRequest::getOrder));
    }

    @Transactional
    public void delete(final Long channelId, final Long memberId) {
        Channel channel = channels.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));

        Member member = members.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        validateSubscriptionExist(channel, member);

        channelSubscriptions.deleteAllByChannelAndMember(channel, member);
    }

    private void validateSubscriptionExist(final Channel channel, final Member member) {
        if (!channelSubscriptions.existsByChannelAndMember(channel, member)) {
            throw new SubscriptionNotExistException(channel.getId());
        }
    }
}
