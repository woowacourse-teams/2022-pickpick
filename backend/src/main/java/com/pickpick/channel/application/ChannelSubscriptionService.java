package com.pickpick.channel.application;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.channel.domain.ChannelSubscription;
import com.pickpick.channel.domain.ChannelSubscriptionRepository;
import com.pickpick.channel.ui.dto.ChannelOrderRequest;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.channel.ui.dto.ChannelSubscriptionRequest;
import com.pickpick.exception.ChannelNotFoundException;
import com.pickpick.exception.MemberNotFoundException;
import com.pickpick.exception.SubscriptionDuplicateException;
import com.pickpick.exception.SubscriptionNotExistException;
import com.pickpick.exception.SubscriptionOrderDuplicateException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<ChannelSubscription> findAllOrderByViewOrder(final Long memberId) {
        return channelSubscriptions.findAllByMemberIdOrderByViewOrder(memberId);
    }

    @Transactional
    public void save(final ChannelSubscriptionRequest subscriptionRequest, final Long memberId) {
        Long channelId = subscriptionRequest.getChannelId();
        Channel channel = channels.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));

        Member member = members.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        validateDuplicatedSubscription(channel, member);

        channelSubscriptions.save(new ChannelSubscription(channel, member, getMaxViewOrder(memberId)));
    }

    private void validateDuplicatedSubscription(final Channel channel, final Member member) {
        if (channelSubscriptions.existsByChannelAndMember(channel, member)) {
            throw new SubscriptionDuplicateException(channel.getId());
        }
    }

    private int getMaxViewOrder(final Long memberId) {
        return channelSubscriptions.findFirstByMemberIdOrderByViewOrderDesc(memberId)
                .map(it -> it.getViewOrder() + ORDER_NEXT)
                .orElse(ORDER_FIRST);
    }

    @Transactional
    public void updateOrders(final List<ChannelOrderRequest> orderRequests, final Long memberId) {
        validateRequest(orderRequests);
        List<ChannelSubscription> subscribedChannels = channelSubscriptions.findAllByMemberId(memberId);
        Map<Long, Integer> ordersByChannelId = getOrdersMap(orderRequests);

        for (ChannelSubscription subscribedChannel : subscribedChannels) {
            subscribedChannel.changeOrder(ordersByChannelId.get(subscribedChannel.getChannelId()));
        }
    }

    private void validateRequest(final List<ChannelOrderRequest> orderRequests) {
        if (isDuplicatedViewOrder(orderRequests)) {
            throw new SubscriptionOrderDuplicateException();
        }
    }

    private boolean isDuplicatedViewOrder(final List<ChannelOrderRequest> orderRequests) {
        return orderRequests.size() != orderRequests.stream()
                .map(ChannelOrderRequest::getOrder)
                .distinct()
                .count();
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
