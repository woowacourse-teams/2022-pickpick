package com.pickpick.channel.application;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.channel.domain.ChannelSubscription;
import com.pickpick.channel.domain.ChannelSubscriptionRepository;
import com.pickpick.channel.ui.dto.ChannelOrderRequest;
import com.pickpick.channel.ui.dto.ChannelSubscriptionRequest;
import com.pickpick.channel.ui.dto.ChannelSubscriptionResponse;
import com.pickpick.channel.ui.dto.ChannelSubscriptionResponses;
import com.pickpick.exception.channel.SubscriptionDuplicateException;
import com.pickpick.exception.channel.SubscriptionNotExistException;
import com.pickpick.exception.channel.SubscriptionOrderDuplicateException;
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

    @Transactional
    public void save(final ChannelSubscriptionRequest subscriptionRequest, final Long memberId) {
        Long channelId = subscriptionRequest.getChannelId();
        Channel channel = channels.getById(channelId);

        Member member = members.getById(memberId);

        validateDuplicatedSubscription(channel, member);

        channelSubscriptions.save(new ChannelSubscription(channel, member, getMaxViewOrder(member)));
    }

    private void validateDuplicatedSubscription(final Channel channel, final Member member) {
        if (channelSubscriptions.existsByChannelAndMember(channel, member)) {
            throw new SubscriptionDuplicateException(channel.getId());
        }
    }

    private int getMaxViewOrder(final Member member) {
        return channelSubscriptions.findFirstByMemberOrderByViewOrderDesc(member)
                .map(it -> it.getViewOrder() + ORDER_NEXT)
                .orElse(ORDER_FIRST);
    }

    public ChannelSubscriptionResponses findByMemberId(final Long memberId) {
        List<ChannelSubscriptionResponse> channelSubscriptionResponses = channelSubscriptions
                .findAllByMemberIdOrderByViewOrder(memberId)
                .stream()
                .map(ChannelSubscriptionResponse::from)
                .collect(Collectors.toList());

        return new ChannelSubscriptionResponses(channelSubscriptionResponses);
    }

    @Transactional
    public void updateOrders(final List<ChannelOrderRequest> orderRequests, final Long memberId) {
        List<ChannelSubscription> subscribedChannels = channelSubscriptions.findAllByMemberId(memberId);
        validateRequest(subscribedChannels, orderRequests);
        Map<Long, Integer> ordersByChannelId = getOrdersMap(orderRequests);

        for (ChannelSubscription subscribedChannel : subscribedChannels) {
            subscribedChannel.changeOrder(ordersByChannelId.get(subscribedChannel.getChannelId()));
        }
    }

    private void validateRequest(final List<ChannelSubscription> subscribedChannels,
                                 final List<ChannelOrderRequest> orderRequests) {
        if (isDuplicatedViewOrder(orderRequests)) {
            throw new SubscriptionOrderDuplicateException();
        }

        if (isUnsubscribedChannelOfMember(subscribedChannels, orderRequests)) {
            throw new SubscriptionNotExistException("멤버가 구독한 적 없는 채널의 순서를 변경할 수 없습니다.");
        }

        if (isEverySubscriptionExceptionNotIncluded(subscribedChannels, orderRequests)) {
            throw new SubscriptionNotExistException("멤버의 모든 구독 채널 아이디가 포함되지 않았습니다.");
        }
    }

    private boolean isDuplicatedViewOrder(final List<ChannelOrderRequest> orderRequests) {
        return orderRequests.size() != orderRequests.stream()
                .map(ChannelOrderRequest::getOrder)
                .distinct()
                .count();
    }

    private boolean isUnsubscribedChannelOfMember(final List<ChannelSubscription> subscribedChannels,
                                                  final List<ChannelOrderRequest> orderRequests) {
        List<Long> subscribedChannelIds = subscribedChannels.stream()
                .map(ChannelSubscription::getChannelId)
                .collect(Collectors.toList());

        return !orderRequests.stream()
                .allMatch(it -> subscribedChannelIds.contains(it.getId()));
    }

    private boolean isEverySubscriptionExceptionNotIncluded(final List<ChannelSubscription> subscribedChannels,
                                                            final List<ChannelOrderRequest> orderRequests) {
        List<Long> requestChannelId = orderRequests.stream()
                .map(ChannelOrderRequest::getId)
                .collect(Collectors.toList());

        return !subscribedChannels.stream()
                .allMatch(it -> requestChannelId.contains(it.getChannelId()));
    }

    private Map<Long, Integer> getOrdersMap(final List<ChannelOrderRequest> orderRequests) {
        return orderRequests.stream()
                .collect(Collectors.toMap(ChannelOrderRequest::getId, ChannelOrderRequest::getOrder));
    }

    @Transactional
    public void delete(final Long channelId, final Long memberId) {
        Channel channel = channels.getById(channelId);

        Member member = members.getById(memberId);

        validateSubscriptionExist(channel, member);

        channelSubscriptions.deleteAllByChannelAndMember(channel, member);
    }

    private void validateSubscriptionExist(final Channel channel, final Member member) {
        if (!channelSubscriptions.existsByChannelAndMember(channel, member)) {
            throw new SubscriptionNotExistException(channel.getId());
        }
    }
}
