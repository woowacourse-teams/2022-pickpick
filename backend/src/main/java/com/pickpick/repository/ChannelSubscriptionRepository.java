package com.pickpick.repository;

import com.pickpick.entity.ChannelSubscription;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ChannelSubscriptionRepository extends Repository<ChannelSubscription, Long> {

    void saveAll(Iterable<ChannelSubscription> channelSubscriptions);

    List<ChannelSubscription> findAllByOrderByViewOrder();

    void deleteAll();

    void deleteByIdIn(List<Long> id);
}
