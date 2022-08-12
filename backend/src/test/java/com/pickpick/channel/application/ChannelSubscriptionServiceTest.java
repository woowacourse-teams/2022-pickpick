package com.pickpick.channel.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.channel.domain.ChannelSubscription;
import com.pickpick.channel.ui.dto.ChannelOrderRequest;
import com.pickpick.channel.ui.dto.ChannelSubscriptionRequest;
import com.pickpick.exception.channel.ChannelNotFoundException;
import com.pickpick.exception.channel.SubscriptionDuplicateException;
import com.pickpick.exception.channel.SubscriptionNotExistException;
import com.pickpick.exception.channel.SubscriptionOrderDuplicateException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ChannelSubscriptionServiceTest {

    private static final long NOT_EXISTED_CHANNEL_ID = 1L;

    @Autowired
    private ChannelSubscriptionService channelSubscriptionService;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private MemberRepository members;

    @DisplayName("채널 구독을 단건 저장")
    @Test
    void save() {
        // given
        Member member = saveMember();
        Channel channel = saveChannel("slackId", "채널 이름");
        subscribeChannel(member, channel);

        // then
        assertThat(channelSubscriptionService.findAllOrderByViewOrder(member.getId())).hasSize(1);
    }

    @DisplayName("존재하지 않는 채널 ID로 채널 저장 시 에러 발생")
    @Test
    void saveByNotExistedChannelId() {
        // given
        Member member = saveMember();
        ChannelSubscriptionRequest request = new ChannelSubscriptionRequest(NOT_EXISTED_CHANNEL_ID);

        // when & then
        assertThatThrownBy(() -> channelSubscriptionService.save(request, member.getId()))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @DisplayName("이미 구독한 채널을 다시 구독 요청 시 에러 발생")
    @Test
    void saveAlreadySubscribedChannel() {
        // given
        Member member = saveMember();
        Channel channel = saveChannel("slackId", "채널 이름");
        subscribeChannel(member, channel);

        // when & then
        assertThatThrownBy(() -> subscribeChannel(member, channel))
                .isInstanceOf(SubscriptionDuplicateException.class);
    }

    @DisplayName("구독한 채널 조회 시 view order 순서대로 출력")
    @Test
    void subscribeChannelOrderIsLast() {
        // given
        Member member = saveMember();
        Channel channel1 = saveChannel("slackId1", "채널 이름1");
        Channel channel2 = saveChannel("slackId2", "채널 이름2");
        Channel channel3 = saveChannel("slackId3", "채널 이름3");

        subscribeChannelsInListOrder(member, List.of(channel3, channel1, channel2));

        // when
        List<ChannelSubscription> channelSubscriptions = channelSubscriptionService.findAllOrderByViewOrder(
                member.getId());

        // then
        assertThat(channelSubscriptions).extracting("channel")
                .containsExactly(channel3, channel1, channel2);
    }

    @DisplayName("채널 구독 순서를 변경하기")
    @Test
    void updateChannelSubscriptionOrder() {
        // given
        Member member = saveMember();
        Channel channel1 = saveChannel("slackId1", "채널 이름1");
        Channel channel2 = saveChannel("slackId2", "채널 이름2");
        Channel channel3 = saveChannel("slackId3", "채널 이름3");

        subscribeChannelsInListOrder(member, List.of(channel1, channel2, channel3));

        // when
        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(channel1.getId(), 1),
                new ChannelOrderRequest(channel2.getId(), 2),
                new ChannelOrderRequest(channel3.getId(), 3)
        );
        channelSubscriptionService.updateOrders(request, member.getId());

        List<ChannelSubscription> channelSubscriptions = channelSubscriptionService.findAllOrderByViewOrder(
                member.getId());

        //then
        assertThat(channelSubscriptions).extracting("channel")
                .containsExactly(channel1, channel2, channel3);
    }

    @DisplayName("채널 구독 순서 변경 시 중복 viewOrder가 들어올 경우 에러 발생")
    @Test
    void updateChannelSubscriptionOrderWithDuplicateViewOrder() {
        // given
        Member member = saveMember();
        Channel channel1 = saveChannel("slackId1", "채널 이름1");
        Channel channel2 = saveChannel("slackId2", "채널 이름2");
        Channel channel3 = saveChannel("slackId3", "채널 이름3");

        subscribeChannelsInListOrder(member, List.of(channel1, channel2, channel3));

        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(channel1.getId(), 1),
                new ChannelOrderRequest(channel2.getId(), 2),
                new ChannelOrderRequest(channel3.getId(), 2)
        );

        // when & then
        assertThatThrownBy(() -> channelSubscriptionService.updateOrders(request, member.getId()))
                .isInstanceOf(SubscriptionOrderDuplicateException.class);
    }

    @DisplayName("채널 구독 순서 변경 시 해당 멤버가 구독한 적 없는 채널 아이디가 들어올 경우 예외 발생")
    @Test
    void updateChannelSubscriptionOrderWithInvalidChannelId() {
        // given
        Member member = saveMember();
        Channel channel1 = saveChannel("slackId1", "채널 이름1");
        Channel channel2 = saveChannel("slackId2", "채널 이름2");
        Channel channel3 = saveChannel("slackId3", "채널 이름3");

        subscribeChannelsInListOrder(member, List.of(channel1, channel2));

        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(channel1.getId(), 1),
                new ChannelOrderRequest(channel2.getId(), 2),
                new ChannelOrderRequest(channel3.getId(), 3)
        );

        // when & then
        assertThatThrownBy(() -> channelSubscriptionService.updateOrders(request, member.getId()))
                .isInstanceOf(SubscriptionNotExistException.class);
    }

    @DisplayName("채널 구독 순서 변경 시 해당 멤버의 모든 구독 채널 아이디가 들어오지 않은 경우 예외 발생")
    @Test
    void updateChannelSubscriptionOrderWithNotEnoughChannelId() {
        // given
        Member member = saveMember();
        Channel channel1 = saveChannel("slackId1", "채널 이름1");
        Channel channel2 = saveChannel("slackId2", "채널 이름2");
        Channel channel3 = saveChannel("slackId3", "채널 이름3");

        subscribeChannelsInListOrder(member, List.of(channel1, channel2, channel3));

        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(channel1.getId(), 1),
                new ChannelOrderRequest(channel2.getId(), 2)
        );

        // when & then
        assertThatThrownBy(() -> channelSubscriptionService.updateOrders(request, member.getId()))
                .isInstanceOf(SubscriptionNotExistException.class);
    }

    @DisplayName("구독 중이 아닌 채널 구독 취소시 예외 발생")
    @Test
    void unsubscribeInvalidChannelSubscription() {
        // given
        Member member = saveMember();
        Channel channel = saveChannel("slackId", "채널 이름");
        subscribeChannel(member, channel);
        channelSubscriptionService.delete(channel.getId(), member.getId());

        // when & then
        assertThatThrownBy(() -> channelSubscriptionService.delete(channel.getId(), member.getId()))
                .isInstanceOf(SubscriptionNotExistException.class);
    }

    @DisplayName("존재하지 않는 채널 아이디로 구독 취소시 예외 발생")
    @Test
    void unsubscribeNotExistedChannel() {
        // given
        Member member = saveMember();

        // when & then
        assertThatThrownBy(() -> channelSubscriptionService.delete(NOT_EXISTED_CHANNEL_ID, member.getId()))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @DisplayName("채널 구독 취소")
    @Test
    void unsubscribeChannel() {
        // given
        Member member = saveMember();
        Channel channel = saveChannel("slackId", "채널 이름");
        subscribeChannel(member, channel);

        // when
        channelSubscriptionService.delete(channel.getId(), member.getId());

        // then
        boolean isSubscribed = channelSubscriptionService.findAll(member.getId())
                .get(0)
                .isSubscribed();

        assertThat(isSubscribed).isFalse();
    }

    private Member saveMember() {
        Member member = members.save(new Member("TESTMEMBER", "테스트 계정", "test.png"));
        return member;
    }

    private Channel saveChannel(final String slackId, final String channelName) {
        Channel channel = channels.save(new Channel(slackId, channelName));
        return channel;
    }

    private void subscribeChannel(Member member, Channel channel) {
        ChannelSubscriptionRequest request = new ChannelSubscriptionRequest(channel.getId());
        channelSubscriptionService.save(request, member.getId());
    }

    private void subscribeChannelsInListOrder(Member member, List<Channel> channels) {
        for (Channel channel : channels) {
            subscribeChannel(member, channel);
        }
    }
}
