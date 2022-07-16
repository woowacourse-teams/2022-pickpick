package com.pickpick.repository;

import com.pickpick.entity.Channel;
import com.pickpick.entity.ChannelSubscription;
import com.pickpick.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ChannelSubscriptionRepository extends Repository<ChannelSubscription, Long> {

    void save(ChannelSubscription channelSubscription);

    void saveAll(Iterable<ChannelSubscription> channelSubscriptions);

    void deleteAllByMemberId(Long memberId);

    List<ChannelSubscription> findAllByMemberId(Long memberId);

    List<ChannelSubscription> findAllByMemberIdOrderByViewOrder(Long memberId);

    Optional<ChannelSubscription> findFirstByMemberIdOrderByViewOrderDesc(Long memberId);

    void deleteAllByChannelAndMember(Channel channel, Member member);
}
