package com.pickpick.repository;

import com.pickpick.entity.ChannelSubscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ChannelSubscriptionRepository extends Repository<ChannelSubscription, Long> {

    void save(ChannelSubscription channelSubscription);

    void saveAll(Iterable<ChannelSubscription> channelSubscriptions);

    List<ChannelSubscription> findAllByOrderByViewOrder();

    void deleteAllByMemberId(Long memberId);

    void deleteByIdIn(List<Long> id);

    List<ChannelSubscription> findAllByMemberId(Long memberId);

    List<ChannelSubscription> findAllByMemberIdOrderByViewOrder(Long memberId);

    Optional<ChannelSubscription> findFirstByMemberIdOrderByViewOrderDesc(Long memberId);
}
