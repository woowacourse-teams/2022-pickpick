package com.pickpick.service;

import com.pickpick.controller.dto.ChannelOrderRequest;
import com.pickpick.controller.dto.ChannelSubscriptionRequest;
import com.pickpick.entity.Channel;
import com.pickpick.entity.ChannelSubscription;
import com.pickpick.entity.Member;
import com.pickpick.exception.ChannelNotFoundException;
import com.pickpick.exception.SubscriptionDuplicatedException;
import com.pickpick.exception.SubscriptionNotExistException;
import com.pickpick.repository.ChannelRepository;
import com.pickpick.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ChannelSubscriptionServiceTest {

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
        ChannelSubscriptionRequest request = new ChannelSubscriptionRequest(channel.getId());

        // when
        channelSubscriptionService.save(request, member.getId());

        // then
        assertThat(channelSubscriptionService.findAllOrderByViewOrder(member.getId())).hasSize(1);
    }

    @DisplayName("존재하지 않는 채널 ID로 채널 저장 시 에러 발생")
    @Test
    void saveByNotExistedChannelId() {
        Member member = saveMember();
        ChannelSubscriptionRequest request = new ChannelSubscriptionRequest(1L);

        assertThatThrownBy(() -> channelSubscriptionService.save(request, member.getId()))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @DisplayName("이미 구독한 채널을 다시 구독 요청 시 에러 발생")
    @Test
    void saveAlreadySubscribedChannel() {
        Member member = saveMember();
        Channel channel = saveChannel("slackId", "채널 이름");
        ChannelSubscriptionRequest request = new ChannelSubscriptionRequest(channel.getId());
        channelSubscriptionService.save(request, member.getId());

        assertThatThrownBy(() -> channelSubscriptionService.save(request, member.getId()))
                .isInstanceOf(SubscriptionDuplicatedException.class);
    }

    @DisplayName("구독한 채널 조회 시 view order 순서대로 출력")
    @Test
    void subscribeChannelOrderIsLast() {
        Member member = saveMember();
        Channel channel1 = saveChannel("slackId1", "채널 이름1");
        Channel channel2 = saveChannel("slackId2", "채널 이름2");
        Channel channel3 = saveChannel("slackId3", "채널 이름3");

        ChannelSubscriptionRequest request1 = new ChannelSubscriptionRequest(channel1.getId());
        ChannelSubscriptionRequest request2 = new ChannelSubscriptionRequest(channel2.getId());
        ChannelSubscriptionRequest request3 = new ChannelSubscriptionRequest(channel3.getId());

        channelSubscriptionService.save(request3, member.getId());
        channelSubscriptionService.save(request1, member.getId());
        channelSubscriptionService.save(request2, member.getId());

        List<ChannelSubscription> channelSubscriptions = channelSubscriptionService.findAllOrderByViewOrder(member.getId());

        assertThat(channelSubscriptions.get(0).getChannel()).isEqualTo(channel3);
        assertThat(channelSubscriptions.get(1).getChannel()).isEqualTo(channel1);
        assertThat(channelSubscriptions.get(2).getChannel()).isEqualTo(channel2);
    }

    @DisplayName("채널 구독 순서를 변경하기")
    @Test
    void updateChannelSubscriptionOrder() {
        // given
        Member member = saveMember();
        Channel channel1 = saveChannel("slackId1", "채널 이름1");
        Channel channel2 = saveChannel("slackId2", "채널 이름2");
        Channel channel3 = saveChannel("slackId3", "채널 이름3");

        ChannelSubscriptionRequest request1 = new ChannelSubscriptionRequest(channel1.getId());
        ChannelSubscriptionRequest request2 = new ChannelSubscriptionRequest(channel2.getId());
        ChannelSubscriptionRequest request3 = new ChannelSubscriptionRequest(channel3.getId());

        channelSubscriptionService.save(request3, member.getId());
        channelSubscriptionService.save(request1, member.getId());
        channelSubscriptionService.save(request2, member.getId());

        // when
        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(channel1.getId(), 1),
                new ChannelOrderRequest(channel2.getId(), 2),
                new ChannelOrderRequest(channel3.getId(), 3)
        );
        channelSubscriptionService.updateOrders(request, member.getId());

        //then
        List<ChannelSubscription> channelSubscriptions = channelSubscriptionService.findAllOrderByViewOrder(member.getId());

        assertThat(channelSubscriptions.get(0).getChannel()).isEqualTo(channel1);
        assertThat(channelSubscriptions.get(1).getChannel()).isEqualTo(channel2);
        assertThat(channelSubscriptions.get(2).getChannel()).isEqualTo(channel3);
    }

    @DisplayName("구독 중이 아닌 채널 구독 취소시 예외 발생")
    @Test
    void unsubscribeInvalidChannelSubscription() {
        Member member = saveMember();
        Channel channel = saveChannel("slackId", "채널 이름");
        ChannelSubscriptionRequest request = new ChannelSubscriptionRequest(channel.getId());
        channelSubscriptionService.save(request, member.getId());
        channelSubscriptionService.delete(channel.getId(), member.getId());

        assertThatThrownBy(() -> channelSubscriptionService.delete(channel.getId(), member.getId()))
                .isInstanceOf(SubscriptionNotExistException.class);
    }

    @DisplayName("존재하지 않는 채널 아이디로 구독 취소시 예외 발생")
    @Test
    void unsubscribeNotExistedChannel() {
        Member member = saveMember();

        assertThatThrownBy(() -> channelSubscriptionService.delete(1L, member.getId()))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @DisplayName("채널 구독 취소")
    @Test
    void unsubscribeChannel() {
        // given
        Member member = saveMember();
        Channel channel = saveChannel("slackId", "채널 이름");
        ChannelSubscriptionRequest request = new ChannelSubscriptionRequest(channel.getId());
        channelSubscriptionService.save(request, member.getId());

        // when
        channelSubscriptionService.delete(channel.getId(), member.getId());

        // then
        assertThat(channelSubscriptionService.findAll(member.getId()).get(0).isSubscribed()).isFalse();
    }

    private Member saveMember() {
        Member member = new Member("TESTMEMBER", "테스트 계정", "test.png");
        members.save(member);
        return member;
    }

    private Channel saveChannel(final String slackId, final String channelName) {
        Channel channel = new Channel(slackId, channelName);
        channels.save(channel);
        return channel;
    }
}
