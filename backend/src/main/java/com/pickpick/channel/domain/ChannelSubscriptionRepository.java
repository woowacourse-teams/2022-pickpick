package com.pickpick.channel.domain;

import com.pickpick.exception.channel.SubscriptionNotFoundException;
import com.pickpick.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface ChannelSubscriptionRepository extends Repository<ChannelSubscription, Long> {

    ChannelSubscription save(ChannelSubscription channelSubscription);

    @Query("select cs from ChannelSubscription cs where cs.member.id = :memberId")
    List<ChannelSubscription> findAllByMemberId(Long memberId);

    @Query("select cs from ChannelSubscription cs where cs.member.id = :memberId order by cs.viewOrder")
    List<ChannelSubscription> findAllByMemberIdOrderByViewOrder(Long memberId);

    Optional<ChannelSubscription> findFirstByMemberOrderByViewOrderDesc(Member member);

    Optional<ChannelSubscription> findFirstByMemberIdOrderByViewOrderAsc(Long memberId);

    boolean existsByChannelAndMember(Channel channel, Member member);

    void deleteAllByChannelAndMember(Channel channel, Member member);

    default ChannelSubscription getFirstByMemberIdOrderByViewOrderAsc(final Long memberId) {
        return findFirstByMemberIdOrderByViewOrderAsc(memberId)
                .orElseThrow(() -> new SubscriptionNotFoundException(memberId));
    }
}
