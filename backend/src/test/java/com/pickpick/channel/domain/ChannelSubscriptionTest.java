package com.pickpick.channel.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pickpick.exception.channel.SubscriptionInvalidOrderException;
import com.pickpick.member.domain.Member;
import com.pickpick.workspace.domain.Workspace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ChannelSubscriptionTest {

    @DisplayName("채널 구독 순서는 1 미만일 수 없다.")
    @ValueSource(ints = {0, -1})
    @ParameterizedTest
    void changeOrder(final int invalidOrder) {
        // given
        Workspace workspace = new Workspace("T000001", "xoxp-token-1234", "UB00001");
        Channel channel = new Channel("slackId", "채널 이름");
        Member member = new Member("slackId", "유저 이름", "Profile.png", workspace);
        ChannelSubscription channelSubscription = new ChannelSubscription(channel, member, 1);

        // when & then
        assertThatThrownBy(() -> channelSubscription.changeOrder(invalidOrder))
                .isInstanceOf(SubscriptionInvalidOrderException.class);
    }
}
