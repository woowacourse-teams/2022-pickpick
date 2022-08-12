package com.pickpick.channel.domain;

import com.pickpick.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ChannelSubscriptionRepository extends Repository<ChannelSubscription, Long> {

    ChannelSubscription save(ChannelSubscription channelSubscription);

    void saveAll(Iterable<ChannelSubscription> channelSubscriptions);

    List<ChannelSubscription> findAllByMemberId(Long memberId);

    List<ChannelSubscription> findAllByMemberIdOrderByViewOrder(Long memberId);

    Optional<ChannelSubscription> findFirstByMemberIdOrderByViewOrderDesc(Long memberId);

    Optional<ChannelSubscription> findFirstByMemberIdOrderByViewOrderAsc(Long memberId);

    boolean existsByChannelAndMember(Channel channel, Member member);

    void deleteAllByMemberId(Long memberId);

    void deleteAllByChannelAndMember(Channel channel, Member member);
}
