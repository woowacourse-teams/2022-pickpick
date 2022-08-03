package com.pickpick.channel.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pickpick.exception.SubscriptionOrderMinException;
import com.pickpick.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ChannelSubscriptionTest {

    @DisplayName("채널 구독 순서는 1 미만일 수 없다.")
    @ValueSource(ints = {0, -1})
    @ParameterizedTest
    void changeOrder(int invalidOrder) {
        // given
        Channel channel = new Channel("slackId", "채널 이름");
        Member member = new Member("slackId", "유저 이름", "Profile.png");
        ChannelSubscription channelSubscription = new ChannelSubscription(channel, member, 1);

        // when & then
        assertThatThrownBy(() -> channelSubscription.changeOrder(invalidOrder))
                .isInstanceOf(SubscriptionOrderMinException.class);
    }
}
