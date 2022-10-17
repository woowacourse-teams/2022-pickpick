package com.pickpick.channel.domain;

import static com.pickpick.fixture.ChannelFixture.QNA;
import static com.pickpick.fixture.MemberFixture.BOM;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
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
        Workspace jupjup = JUPJUP.create();
        Channel qna = QNA.create();
        Member bom = BOM.create(jupjup);
        ChannelSubscription channelSubscription = new ChannelSubscription(qna, bom, 1);

        // when & then
        assertThatThrownBy(() -> channelSubscription.changeOrder(invalidOrder))
                .isInstanceOf(SubscriptionInvalidOrderException.class);
    }
}
