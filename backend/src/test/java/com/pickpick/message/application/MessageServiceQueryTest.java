package com.pickpick.message.application;

import static com.pickpick.fixture.MessageRequestFactory.emptyQueryParams;
import static com.pickpick.fixture.MessageRequestFactory.emptyQueryParamsWithCount;
import static com.pickpick.fixture.MessageRequestFactory.fromLatestInChannels;
import static com.pickpick.fixture.MessageRequestFactory.futureFromTargetMessageInChannels;
import static com.pickpick.fixture.MessageRequestFactory.pastFromTargetMessageInChannels;
import static com.pickpick.fixture.MessageRequestFactory.searchByKeywordInChannels;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.channel.domain.ChannelSubscription;
import com.pickpick.channel.domain.ChannelSubscriptionRepository;
import com.pickpick.fixture.MessageFixtures;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.ui.dto.MessageRequest;
import com.pickpick.message.ui.dto.MessageResponse;
import com.pickpick.message.ui.dto.MessageResponses;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql({"/truncate.sql"})
@Transactional
@SpringBootTest
class MessageServiceQueryTest {

    private static final int MESSAGE_COUNT = 5;
    private static final int MESSAGE_COUNT_DEFAULT = 20;
    private static final String MESSAGE_KEYWORD = "줍줍";
    private static final int TARGET_INDEX = 15;
    private static final int VIEW_ORDER_FIRST = 1;
    private static final int VIEW_ORDER_SECOND = 2;

    @Autowired
    private MemberRepository members;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private ChannelSubscriptionRepository subscriptions;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private MessageService messageService;

    @DisplayName("쿼리 파라미터와 count가 없다면, 회원의 첫번째 순서 구독 채널의 최신 메시지를 20개 내림차순 조회한다")
    @Test
    void findMessagesEmptyParameters() {
        // given
        Member summer = members.save(summer());
        Channel notice = channels.save(notice());
        messages.saveAll(createMessages(notice, summer));

        Channel freeChat = channels.save(freeChat());
        List<Message> messagesInFreeChat = messages.saveAll(createMessages(freeChat, summer));

        subscriptions.save(new ChannelSubscription(freeChat, summer, VIEW_ORDER_FIRST));
        subscriptions.save(new ChannelSubscription(notice, summer, VIEW_ORDER_SECOND));

        MessageRequest request = emptyQueryParams();

        // when
        MessageResponses response = messageService.find(summer.getId(), request);

        // then
        List<MessageResponse> foundMessages = response.getMessages();
        List<Long> expectedIds = expectedOrderedIds(messagesInFreeChat, MESSAGE_COUNT_DEFAULT);

        assertAll(
                () -> assertThat(foundMessages).extracting("id").isEqualTo(expectedIds),
                () -> assertThat(foundMessages).hasSize(MESSAGE_COUNT_DEFAULT)
        );
    }

    @DisplayName("쿼리 파라미터가 없고 count가 있다면, 회원의 첫번째 순서 구독 채널의 최신 메시지를 count개 내림차순 조회한다")
    @Test
    void findMessagesEmptyParametersWithCount() {
        // given
        Member summer = members.save(summer());
        Channel notice = channels.save(notice());
        messages.saveAll(createMessages(notice, summer));

        Channel freeChat = channels.save(freeChat());
        List<Message> messagesInFreeChat = messages.saveAll(createMessages(freeChat, summer));

        subscriptions.save(new ChannelSubscription(freeChat, summer, VIEW_ORDER_FIRST));
        subscriptions.save(new ChannelSubscription(notice, summer, VIEW_ORDER_SECOND));

        MessageRequest request = emptyQueryParamsWithCount(MESSAGE_COUNT);

        // when
        MessageResponses response = messageService.find(summer.getId(), request);

        // then
        List<MessageResponse> foundMessages = response.getMessages();
        List<Long> expectedIds = expectedOrderedIds(messagesInFreeChat, MESSAGE_COUNT);

        assertAll(
                () -> assertThat(foundMessages).extracting("id").isEqualTo(expectedIds),
                () -> assertThat(foundMessages).hasSize(MESSAGE_COUNT)
        );
    }

    @DisplayName("더 이상 조회할 메시지가 없다면 isLast로 true를 반환한다")
    @Test
    void findMessagesIsLast() {
        // given
        Member summer = members.save(summer());
        Channel notice = channels.save(notice());
        List<Message> messagesInNotice = messages.saveAll(createMessages(notice, summer));
        subscriptions.save(new ChannelSubscription(notice, summer, VIEW_ORDER_FIRST));

        MessageRequest request = emptyQueryParamsWithCount(messagesInNotice.size());

        // when
        MessageResponses response = messageService.find(summer.getId(), request);
        boolean isLast = response.isLast();

        // then
        assertThat(isLast).isEqualTo(true);
    }

    @DisplayName("메시지 조회 시, 텍스트가 비어있는 메시지는 필터링된다")
    @Test
    void emptyMessagesShouldBeFiltered() {
        // given
        Member summer = members.save(summer());
        Channel notice = channels.save(notice());
        List<Message> messagesInNotice = messages.saveAll(createMessages(notice, summer));

        MessageRequest request = fromLatestInChannels(List.of(notice), messagesInNotice.size());

        // when
        MessageResponses messageResponses = messageService.find(summer.getId(), request);

        // then
        List<MessageResponse> foundMessages = messageResponses.getMessages();
        boolean isEmptyMessageFiltered = foundMessages.stream()
                .noneMatch(message -> message.getText().isEmpty());

        assertThat(isEmptyMessageFiltered).isTrue();
    }

    @DisplayName("여러 채널의 특정 키워드로 조회 시, 해당 키워드가 존재하는 메시지만 조회된다")
    @Test
    void findMessagesInChannelsWithKeyword() {
        // given
        Member summer = members.save(summer());
        Channel notice = channels.save(notice());
        Channel freeChat = channels.save(freeChat());

        messages.save(MessageFixtures.PLAIN_20220712_15_00_00.create(freeChat, summer));

        messages.save(MessageFixtures.KEYWORD_20220714_14_00_00.create(notice, summer));
        messages.save(MessageFixtures.KEYWORD_20220714_14_00_00.create(freeChat, summer));

        MessageRequest request = searchByKeywordInChannels(List.of(notice, freeChat), MESSAGE_KEYWORD, MESSAGE_COUNT);

        // when
        MessageResponses response = messageService.find(summer.getId(), request);

        // then
        List<MessageResponse> foundMessages = response.getMessages();
        boolean isContainingKeyword = foundMessages.stream()
                .allMatch(message -> message.getText().contains(MESSAGE_KEYWORD));

        assertAll(
                () -> assertThat(foundMessages).hasSize(2),
                () -> assertThat(isContainingKeyword).isTrue()
        );
    }

    @DisplayName("채널과 메시지ID로 앞선 메시지 조회 시, 메시지 기준 해당 채널의 미래 메시지를 시간 내림차순 조회한다")
    @Test
    void findMessagesWithChannelAndMessageNotNeedPast() {
        // given
        Member summer = members.save(summer());
        Channel notice = channels.save(notice());
        List<Message> messagesInNotice = messages.saveAll(createMessages(notice, summer));
        Message targetMessage = messagesInNotice.get(TARGET_INDEX);

        MessageRequest request = futureFromTargetMessageInChannels(List.of(notice), targetMessage, MESSAGE_COUNT);

        // when
        MessageResponses response = messageService.find(summer.getId(), request);

        // then
        List<MessageResponse> foundMessages = response.getMessages();
        List<Long> expectedIds = expectedFutureOrderedIds(messagesInNotice, targetMessage, MESSAGE_COUNT);
        boolean isFutureMessages = foundMessages.stream()
                .allMatch(message -> message.getPostedDate().isAfter(targetMessage.getPostedDate()));

        assertAll(
                () -> assertThat(foundMessages).extracting("id").containsExactlyElementsOf(expectedIds),
                () -> assertThat(isFutureMessages).isTrue()
        );
    }

    @DisplayName("채널과 메시지ID로 지난 메시지 조회 시, 메시지 기준 해당 채널의 과거 메시지를 시간 내림차순 조회한다")
    @Test
    void findMessagesWithChannelAndMessageNeedPast() {
        // given
        Member summer = members.save(summer());
        Channel notice = channels.save(notice());
        List<Message> messagesInNotice = messages.saveAll(createMessages(notice, summer));
        Message targetMessage = messagesInNotice.get(TARGET_INDEX);

        MessageRequest request = pastFromTargetMessageInChannels(List.of(notice), targetMessage, MESSAGE_COUNT);

        // when
        MessageResponses response = messageService.find(summer.getId(), request);

        // then
        List<MessageResponse> foundMessages = response.getMessages();
        List<Long> expectedIds = expectedPastOrderedIds(messagesInNotice, targetMessage, MESSAGE_COUNT);
        boolean isPastMessages = foundMessages.stream()
                .allMatch(message -> message.getPostedDate().isBefore(targetMessage.getPostedDate()));

        assertAll(
                () -> assertThat(foundMessages).extracting("id").containsExactlyElementsOf(expectedIds),
                () -> assertThat(isPastMessages).isTrue()
        );
    }

    private Member summer() {
        return new Member("U00001", "써머", "https://summer.png");
    }

    private Channel notice() {
        return new Channel("C00001", "공지사항");
    }

    private Channel freeChat() {
        return new Channel("C00002", "잡담");
    }

    private List<Message> createMessages(final Channel channel, final Member member) {
        return Arrays.stream(MessageFixtures.values())
                .map(messageFixture -> messageFixture.create(channel, member))
                .collect(Collectors.toList());
    }

    private List<Long> expectedOrderedIds(final List<Message> savedMessages, final int count) {
        List<Long> foundPastMessagesIds = savedMessages.stream()
                .filter(message -> !message.getText().isEmpty())
                .sorted(Comparator.comparing(Message::getPostedDate).reversed())
                .map(Message::getId)
                .collect(Collectors.toList());

        return foundPastMessagesIds.subList(0, count);
    }

    private List<Long> expectedPastOrderedIds(final List<Message> savedMessages, final Message targetMessage,
                                              final int count) {
        List<Long> foundPastMessagesIds = savedMessages.stream()
                .filter(message -> !message.getText().isEmpty()
                        && message.getPostedDate().isBefore(targetMessage.getPostedDate()))
                .sorted(Comparator.comparing(Message::getPostedDate).reversed())
                .map(Message::getId)
                .collect(Collectors.toList());

        return foundPastMessagesIds.subList(0, count);
    }

    private List<Long> expectedFutureOrderedIds(final List<Message> savedMessages, final Message targetMessage,
                                                final int count) {
        List<Long> foundFutureMessagesIds = savedMessages.stream()
                .filter(message -> !message.getText().isEmpty()
                        && message.getPostedDate().isAfter(targetMessage.getPostedDate()))
                .sorted(Comparator.comparing(Message::getPostedDate))
                .map(Message::getId)
                .collect(Collectors.toList());

        List<Long> expectedIds = foundFutureMessagesIds.subList(0, count);
        Collections.reverse(expectedIds);

        return expectedIds;
    }
}
