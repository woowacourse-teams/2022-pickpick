package com.pickpick.repository;

import com.pickpick.entity.ChannelSubscription;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface ChannelSubscriptionRepository extends Repository<ChannelSubscription, Long> {

    void saveAll(Iterable<ChannelSubscription> channelSubscriptions);

    List<ChannelSubscription> findAllByOrderByViewOrder();

    void deleteAllByMemberId(Long memberId);

    void deleteByIdIn(List<Long> id);

    List<ChannelSubscription> findAllByMemberId(Long memberId);

}
