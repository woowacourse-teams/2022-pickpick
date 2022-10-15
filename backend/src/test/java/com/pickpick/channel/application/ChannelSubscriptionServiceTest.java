package com.pickpick.channel.application;

import static com.pickpick.fixture.ChannelFixture.FREE_CHAT;
import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.ChannelFixture.QNA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.channel.domain.ChannelSubscriptionRepository;
import com.pickpick.channel.ui.dto.ChannelOrderRequest;
import com.pickpick.channel.ui.dto.ChannelSubscriptionRequest;
import com.pickpick.channel.ui.dto.ChannelSubscriptionResponse;
import com.pickpick.channel.ui.dto.ChannelSubscriptionResponses;
import com.pickpick.exception.channel.ChannelNotFoundException;
import com.pickpick.exception.channel.SubscriptionDuplicateException;
import com.pickpick.exception.channel.SubscriptionNotExistException;
import com.pickpick.exception.channel.SubscriptionOrderDuplicateException;
import com.pickpick.fixture.MemberFixture;
import com.pickpick.fixture.WorkspaceFixture;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.support.TestConfig;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestConfig.class)
@SpringBootTest
class ChannelSubscriptionServiceTest {

    private static final long NOT_EXISTED_CHANNEL_ID = 1L;

    @Autowired
    private ChannelSubscriptionService channelSubscriptionService;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private ChannelSubscriptionRepository channelSubscriptions;

    @Autowired
    private MemberRepository members;

    @Autowired
    private WorkspaceRepository workspaces;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("채널 구독을 단건 저장")
    @Test
    void save() {
        //1  given
        Member bom = members.save(bom());
        Long bomId = bom.getId();
        Channel notice = channels.save(NOTICE.create());

        ChannelSubscriptionResponses subscriptionsBeforeSave = channelSubscriptionService.findByMemberId(bomId);

        // when
        ChannelSubscriptionRequest request = new ChannelSubscriptionRequest(notice.getId());
        channelSubscriptionService.save(request, bomId);

        ChannelSubscriptionResponses subscriptionsAfterSave = channelSubscriptionService.findByMemberId(bomId);

        // then
        assertAll(
                () -> assertThat(subscriptionsBeforeSave.getChannels()).isEmpty(),
                () -> assertThat(subscriptionsAfterSave.getChannels()).hasSize(1)
        );
    }

    @DisplayName("존재하지 않는 채널 ID로 구독 요청 시 에러 발생")
    @Test
    void saveByNotExistedChannelId() {
        // given
        Member bom = members.save(bom());
        ChannelSubscriptionRequest request = new ChannelSubscriptionRequest(NOT_EXISTED_CHANNEL_ID);

        // when & then
        assertThatThrownBy(() -> channelSubscriptionService.save(request, bom.getId()))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @DisplayName("이미 구독한 채널을 다시 구독 요청 시 에러 발생")
    @Test
    void saveAlreadySubscribedChannel() {
        // given
        Member bom = members.save(bom());
        Channel notice = channels.save(NOTICE.create());
        subscribeChannel(bom, notice);

        // when & then
        assertThatThrownBy(() -> subscribeChannel(bom, notice))
                .isInstanceOf(SubscriptionDuplicateException.class);
    }

    @DisplayName("구독 요청 순서대로 채널의 view order 지정")
    @Test
    void setViewOrderByRequestOrder() {
        // given
        Member bom = members.save(bom());
        Channel notice = channels.save(NOTICE.create());
        Channel freeChat = channels.save(FREE_CHAT.create());

        // when
        subscribeChannel(bom, notice);
        subscribeChannel(bom, freeChat);

        List<ChannelSubscriptionResponse> foundChannels = channelSubscriptionService
                .findByMemberId(bom.getId())
                .getChannels();

        // then
        ChannelSubscriptionResponse firstSubscribed = extractTargetChannel(foundChannels, notice);
        ChannelSubscriptionResponse secondSubscribed = extractTargetChannel(foundChannels, freeChat);

        assertAll(
                () -> assertThat(firstSubscribed.getOrder()).isEqualTo(1),
                () -> assertThat(secondSubscribed.getOrder()).isEqualTo(2)
        );
    }

    @DisplayName("구독한 채널 조회 시 view order 순서대로 출력")
    @Test
    void subscribeChannelOrderIsLast() {
        // given
        Member bom = members.save(bom());
        Channel notice = channels.save(NOTICE.create());
        Channel freeChat = channels.save(FREE_CHAT.create());
        Channel qna = channels.save(QNA.create());

        subscribeChannelsInListOrder(bom, List.of(qna, notice, freeChat));

        // when
        List<ChannelSubscriptionResponse> channelSubscriptions = channelSubscriptionService
                .findByMemberId(bom.getId())
                .getChannels();

        // then
        assertThat(channelSubscriptions).extracting("id")
                .containsExactly(qna.getId(), notice.getId(), freeChat.getId());
    }

    @DisplayName("채널 구독 순서를 변경하기")
    @Test
    void updateChannelSubscriptionOrder() {
        // given
        Member bom = members.save(bom());
        Channel notice = channels.save(NOTICE.create());
        Channel freeChat = channels.save(FREE_CHAT.create());
        Channel qna = channels.save(QNA.create());

        subscribeChannelsInListOrder(bom, List.of(notice, freeChat, qna));

        // when
        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(freeChat.getId(), 1),
                new ChannelOrderRequest(notice.getId(), 2),
                new ChannelOrderRequest(qna.getId(), 3)
        );
        channelSubscriptionService.updateOrders(request, bom.getId());

        List<ChannelSubscriptionResponse> channelSubscriptions = channelSubscriptionService
                .findByMemberId(bom.getId())
                .getChannels();

        //then
        assertThat(channelSubscriptions).extracting("id")
                .containsExactly(freeChat.getId(), notice.getId(), qna.getId());
    }

    @DisplayName("채널 구독 순서 변경 시 중복 view order 가 들어올 경우 에러 발생")
    @Test
    void updateChannelSubscriptionOrderWithDuplicateViewOrder() {
        // given
        Member bom = members.save(bom());
        Channel notice = channels.save(NOTICE.create());
        Channel freeChat = channels.save(FREE_CHAT.create());
        Channel qna = channels.save(QNA.create());

        subscribeChannelsInListOrder(bom, List.of(notice, freeChat, qna));

        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(notice.getId(), 1),
                new ChannelOrderRequest(freeChat.getId(), 2),
                new ChannelOrderRequest(qna.getId(), 2)
        );

        // when & then
        assertThatThrownBy(() -> channelSubscriptionService.updateOrders(request, bom.getId()))
                .isInstanceOf(SubscriptionOrderDuplicateException.class);
    }

    @DisplayName("채널 구독 순서 변경 시 해당 멤버가 구독한 적 없는 채널 아이디가 들어올 경우 예외 발생")
    @Test
    void updateChannelSubscriptionOrderWithInvalidChannelId() {
        // given
        Member bom = members.save(bom());
        Channel notice = channels.save(NOTICE.create());
        Channel freeChat = channels.save(FREE_CHAT.create());
        Channel unsubscribed = channels.save(QNA.create());

        subscribeChannelsInListOrder(bom, List.of(notice, freeChat));

        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(notice.getId(), 1),
                new ChannelOrderRequest(freeChat.getId(), 2),
                new ChannelOrderRequest(unsubscribed.getId(), 3)
        );

        // when & then
        assertThatThrownBy(() -> channelSubscriptionService.updateOrders(request, bom.getId()))
                .isInstanceOf(SubscriptionNotExistException.class);
    }

    @DisplayName("채널 구독 순서 변경 시 해당 멤버의 모든 구독 채널 아이디가 들어오지 않은 경우 예외 발생")
    @Test
    void updateChannelSubscriptionOrderWithNotEnoughChannelId() {
        // given
        Member bom = members.save(bom());
        Channel notice = channels.save(NOTICE.create());
        Channel freeChat = channels.save(FREE_CHAT.create());
        Channel qna = channels.save(QNA.create());

        subscribeChannelsInListOrder(bom, List.of(notice, freeChat, qna));

        List<ChannelOrderRequest> request = List.of(
                new ChannelOrderRequest(notice.getId(), 1),
                new ChannelOrderRequest(freeChat.getId(), 2)
        );

        // when & then
        assertThatThrownBy(() -> channelSubscriptionService.updateOrders(request, bom.getId()))
                .isInstanceOf(SubscriptionNotExistException.class);
    }

    @DisplayName("채널 구독 취소")
    @Test
    void unsubscribeChannel() {
        // given
        Member bom = members.save(bom());
        Channel notice = channels.save(NOTICE.create());
        subscribeChannel(bom, notice);

        boolean isExistBeforeUnsubscribe = channelSubscriptions.existsByChannelAndMember(notice, bom);

        // when
        channelSubscriptionService.delete(notice.getId(), bom.getId());

        // then
        boolean isExistAfterUnsubscribe = channelSubscriptions.existsByChannelAndMember(notice, bom);

        assertAll(
                () -> assertThat(isExistBeforeUnsubscribe).isTrue(),
                () -> assertThat(isExistAfterUnsubscribe).isFalse()
        );
    }

    @DisplayName("구독 중이 아닌 채널 구독 취소시 예외 발생")
    @Test
    void unsubscribeInvalidChannelSubscription() {
        // given
        Member bom = members.save(bom());
        Channel notice = channels.save(NOTICE.create());

        // when & then
        assertThatThrownBy(() -> channelSubscriptionService.delete(notice.getId(), bom.getId()))
                .isInstanceOf(SubscriptionNotExistException.class);
    }

    @DisplayName("존재하지 않는 채널 아이디로 구독 취소시 예외 발생")
    @Test
    void unsubscribeNotExistedChannel() {
        // given
        Member bom = members.save(bom());

        // when & then
        assertThatThrownBy(() -> channelSubscriptionService.delete(NOT_EXISTED_CHANNEL_ID, bom.getId()))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    private Member bom() {
        Workspace workspace = workspaces.save(WorkspaceFixture.JUPJUP.create());
        Member member = MemberFixture.BOM.create(workspace);
        member.firstLogin("xoxp-token");
        return members.save(member);
    }

    private void subscribeChannel(final Member member, final Channel channel) {
        ChannelSubscriptionRequest request = new ChannelSubscriptionRequest(channel.getId());
        channelSubscriptionService.save(request, member.getId());
    }

    private ChannelSubscriptionResponse extractTargetChannel(final List<ChannelSubscriptionResponse> foundChannels,
                                                             final Channel target) {
        return foundChannels.stream()
                .filter(channel -> channel.getId().equals(target.getId()))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }

    private void subscribeChannelsInListOrder(final Member member, final List<Channel> channels) {
        for (Channel channel : channels) {
            subscribeChannel(member, channel);
        }
    }
}
