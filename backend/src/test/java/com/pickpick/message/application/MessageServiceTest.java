package com.pickpick.message.application;

import static com.pickpick.fixture.MessageFixtures.PLAIN_20220712_15_00_00;
import static com.pickpick.fixture.MessageRequestFactory.emptyQueryParams;
import static com.pickpick.fixture.MessageRequestFactory.fromLatestInChannels;
import static com.pickpick.fixture.MessageRequestFactory.futureFromTargetMessageInChannels;
import static com.pickpick.fixture.MessageRequestFactory.onlyCount;
import static com.pickpick.fixture.MessageRequestFactory.pastFromTargetMessageInChannels;
import static com.pickpick.fixture.MessageRequestFactory.searchByKeywordInChannels;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.channel.domain.ChannelSubscription;
import com.pickpick.channel.domain.ChannelSubscriptionRepository;
import com.pickpick.config.DatabaseCleaner;
import com.pickpick.fixture.MessageFixtures;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Bookmark;
import com.pickpick.message.domain.BookmarkRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.domain.Reminder;
import com.pickpick.message.domain.ReminderRepository;
import com.pickpick.message.ui.dto.MessageRequest;
import com.pickpick.message.ui.dto.MessageResponse;
import com.pickpick.message.ui.dto.MessageResponses;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
class MessageServiceTest {

    private static final int MESSAGE_COUNT = 5;
    private static final int MESSAGE_COUNT_DEFAULT = 20;
    private static final int MESSAGE_COUNT_OVER_TOTAL_SIZE = 100;
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
    private BookmarkRepository bookmarks;

    @Autowired
    private ReminderRepository reminders;

    @Autowired
    private MessageService messageService;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @SpyBean
    private Clock clock;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("메시지 조회 시")
    @Nested
    class find {

        Member summer = members.save(summer());
        Channel notice = channels.save(notice());
        Channel freeChat = channels.save(freeChat());

        List<Message> noticeMessages = createAndSaveMessages(notice, summer);
        List<Message> freeChatMessages = createAndSaveMessages(freeChat, summer);
        List<Message> allMessages = messages.findAll();

        ChannelSubscription first = subscriptions.save(new ChannelSubscription(notice, summer, VIEW_ORDER_FIRST));
        ChannelSubscription second = subscriptions.save(new ChannelSubscription(freeChat, summer, VIEW_ORDER_SECOND));

        @DisplayName("조회 조건과 관련없이")
        @Nested
        class alwaysFilter {

            MessageRequest request = fromLatestInChannels(List.of(notice), allMessages.size() + 1);
            MessageResponses response = messageService.find(summer.getId(), request);

            @DisplayName("텍스트가 비어있는 메시지는 제외된다")
            @Test
            void filterEmptyMessages() {
                List<MessageResponse> foundMessages = response.getMessages();
                boolean isEmptyMessageFiltered = foundMessages.stream()
                        .noneMatch(message -> message.getText().isEmpty());

                assertThat(isEmptyMessageFiltered).isTrue();
            }

        }

        @DisplayName("조회 할 과거 메시지가 남아 있다면")
        @Nested
        class pastMessagesRemain {

            MessageRequest request = onlyCount(MESSAGE_COUNT);
            MessageResponses response = messageService.find(summer.getId(), request);

            @DisplayName("hasPast는 true다")
            @Test
            void messagesHasPastTrue() {
                boolean hasPast = response.hasPast();

                assertThat(hasPast).isTrue();
            }

        }

        @DisplayName("조회 할 과거 메시지가 더 이상 없다면")
        @Nested
        class noMorePastMessagesRemain {

            MessageRequest request = onlyCount(noticeMessages.size());
            MessageResponses response = messageService.find(summer.getId(), request);

            @DisplayName("hasPast는 false이다")
            @Test
            void messagesHasPastFalse() {
                boolean hasPast = response.hasPast();

                assertThat(hasPast).isFalse();
            }

        }

        @DisplayName("조회 할 미래 메시지가 남아 있다면")
        @Nested
        class futureMessagesRemain {

            MessageRequest request = pastFromTargetMessageInChannels(
                    List.of(notice), noticeMessages.get(noticeMessages.size() - 1), MESSAGE_COUNT);
            MessageResponses response = messageService.find(summer.getId(), request);

            @DisplayName("hasFuture는 true이다")
            @Test
            void messagesHasFutureTrue() {
                boolean hasFuture = response.hasFuture();

                assertThat(hasFuture).isTrue();
            }
        }

        @DisplayName("조회 할 미래 메시지가 더 이상 없다면")
        @Nested
        class noMoreFutureMessagesRemain {

            MessageRequest request = onlyCount(MESSAGE_COUNT);
            MessageResponses response = messageService.find(summer.getId(), request);

            @DisplayName("hasFutre는 false이다")
            @Test
            void messagesHasFutureFalse() {
                boolean hasFuture = response.hasFuture();

                assertThat(hasFuture).isFalse();
            }
        }

        @DisplayName("특정 단어로 여러 채널에서 검색한다면")
        @Nested
        class keywordInChannels {

            String keyword = "줍줍";

            MessageRequest request = searchByKeywordInChannels(List.of(notice, freeChat), keyword, allMessages.size());
            MessageResponses response = messageService.find(summer.getId(), request);

            @DisplayName("해당 채널들에서 특정 단어가 포함된 메시지가 조회된다")
            @Test
            void messagesContainKeywordInChannels() {
                List<MessageResponse> foundMessages = response.getMessages();
                boolean containsKeyword = foundMessages.stream()
                        .allMatch(message -> message.getText().contains(keyword));

                assertThat(containsKeyword).isTrue();
            }

        }

        @DisplayName("쿼리 파라미터가 하나도 없다면")
        @Nested
        class emptyParameters {

            MessageRequest request = emptyQueryParams();
            MessageResponses response = messageService.find(summer.getId(), request);

            @DisplayName("회원의 첫번째 순서 구독 채널의 최신 메시지를 20개 작성시간 내림차순 조회한다")
            @Test
            void lastTwentyMessagesInFirstSubscription() {
                List<MessageResponse> foundMessages = response.getMessages();
                List<Long> expectedIds = extractDateOrderedIds(noticeMessages, MESSAGE_COUNT_DEFAULT);

                assertAll(
                        () -> assertThat(foundMessages).extracting("id").isEqualTo(expectedIds),
                        () -> assertThat(foundMessages).hasSize(MESSAGE_COUNT_DEFAULT)
                );
            }

        }

        @DisplayName("쿼리 파라미터 중 count만 존재한다면")
        @Nested
        class onlyCountInParameters {

            MessageRequest request = onlyCount(MESSAGE_COUNT);
            MessageResponses response = messageService.find(summer.getId(), request);

            @DisplayName("회원의 첫번째 순서 구독 채널의 최신 메시지를 count개 작성시간 내림차순 조회한다")
            @Test
            void countMessagesInFirstSubscription() {
                List<MessageResponse> foundMessages = response.getMessages();
                List<Long> expectedIds = extractDateOrderedIds(noticeMessages, MESSAGE_COUNT);

                assertAll(
                        () -> assertThat(foundMessages).extracting("id").isEqualTo(expectedIds),
                        () -> assertThat(foundMessages).hasSize(MESSAGE_COUNT)
                );
            }

        }

        @DisplayName("쿼리 파라미터에 하나의 채널 ID가 있다면")
        @Nested
        class oneChannelIdInParameter {

            MessageRequest request = fromLatestInChannels(List.of(freeChat), allMessages.size());
            MessageResponses response = messageService.find(summer.getId(), request);

            @DisplayName("해당 채널에 존재하는 메시지만 조회한다")
            @Test
            void filteredByChannel() {
                List<MessageResponse> foundMessages = response.getMessages();
                List<Long> noticeMessagesId = extractIds(noticeMessages);
                List<Long> freeChatMessagesId = extractIds(freeChatMessages);

                assertAll(
                        () -> assertThat(foundMessages).extracting("id").doesNotContainAnyElementsOf(noticeMessagesId),
                        () -> assertThat(foundMessages).extracting("id").containsAll(freeChatMessagesId)
                );
            }

        }

        @DisplayName("쿼리 파라미터에 복수의 채널 ID가 있다면")
        @Nested
        class multipleChannelIdsInParameters {

            Channel qna = channels.save(new Channel("C00003", "질문과 답변"));
            List<Message> qnaMessages = createAndSaveMessages(qna, summer);

            MessageRequest request = fromLatestInChannels(List.of(notice, freeChat), MESSAGE_COUNT_OVER_TOTAL_SIZE);
            MessageResponses response = messageService.find(summer.getId(), request);

            @DisplayName("해당 복수의 채널에 존재하는 메시지만 조회한다")
            @Test
            void filteredByChannels() {
                List<MessageResponse> foundMessages = response.getMessages();
                List<Long> noticeMessagesId = extractIds(noticeMessages);
                List<Long> freeChatMessagesId = extractIds(freeChatMessages);
                List<Long> qnaMessagesId = extractIds(qnaMessages);

                assertAll(
                        () -> assertThat(foundMessages).extracting("id").containsAll(noticeMessagesId),
                        () -> assertThat(foundMessages).extracting("id").containsAll(freeChatMessagesId),
                        () -> assertThat(foundMessages).extracting("id").doesNotContainAnyElementsOf(qnaMessagesId)
                );
            }

        }

        @DisplayName("쿼리 파라미터에 채널ID와 메세지ID가 존재하고, 앞선 메시지를 조회한다면")
        @Nested
        class channelIdAndMessageIdNeedPastMessageFalseInParameters {

            Message targetMessage = noticeMessages.get(TARGET_INDEX);

            MessageRequest request = futureFromTargetMessageInChannels(List.of(notice), targetMessage, MESSAGE_COUNT);
            MessageResponses response = messageService.find(summer.getId(), request);

            @DisplayName("메시지 기준 해당 채널의 미래 메시지를 작성시간 내림차순 조회한다 ")
            @Test
            void messagesAfterTargetMessageInChannel() {
                List<MessageResponse> foundMessages = response.getMessages();
                List<Long> expectedIds = extractIdsCreatedAfterTarget(noticeMessages, targetMessage,
                        MESSAGE_COUNT);
                boolean isFutureMessages = foundMessages.stream()
                        .allMatch(message -> message.getPostedDate().isAfter(targetMessage.getPostedDate()));

                assertAll(
                        () -> assertThat(foundMessages).extracting("id").containsExactlyElementsOf(expectedIds),
                        () -> assertThat(isFutureMessages).isTrue()
                );
            }

        }

        @DisplayName("쿼리 파라미터에 채널ID와 메시지ID가 존재하고, 지난 메시지를 조회한다면")
        @Nested
        class channelIdAndMessageIdInParametersNeedPastMessageIsTrue {

            Message targetMessage = noticeMessages.get(TARGET_INDEX);

            MessageRequest request = pastFromTargetMessageInChannels(List.of(notice), targetMessage, MESSAGE_COUNT);
            MessageResponses response = messageService.find(summer.getId(), request);

            @DisplayName("메시지 기준 해당 채널의 과거 메시지를 작성시간 내림차순 조회한다")
            @Test
            void pastMessagesInChannelFromTargetMessage() {
                List<MessageResponse> foundMessages = response.getMessages();
                List<Long> expectedIds = extractIdsCreatedBeforeTarget(noticeMessages, targetMessage, MESSAGE_COUNT);
                boolean isPastMessages = foundMessages.stream()
                        .allMatch(message -> message.getPostedDate().isBefore(targetMessage.getPostedDate()));

                assertAll(
                        () -> assertThat(foundMessages).extracting("id").containsExactlyElementsOf(expectedIds),
                        () -> assertThat(isPastMessages).isTrue()
                );
            }

        }

        private List<Message> createAndSaveMessages(final Channel channel, final Member member) {
            List<Message> messagesInChannel = Arrays.stream(MessageFixtures.values())
                    .map(messageFixture -> messageFixture.create(channel, member))
                    .collect(Collectors.toList());

            for (Message fixture : messagesInChannel) {
                messages.save(fixture);
            }

            return messagesInChannel;
        }
    }

    @DisplayName("북마크가 등록되지 않은 메시지라면, isBookmarked가 false 이다")
    @Test
    void isBookmarkedFalseIfNotBookmarked() {
        // given
        Member summer = members.save(summer());
        Channel notice = channels.save(notice());
        subscriptions.save(new ChannelSubscription(notice, summer, VIEW_ORDER_FIRST));

        Message message = messages.save(PLAIN_20220712_15_00_00.create(notice, summer));
        bookmarks.save(new Bookmark(summer, message));

        MessageRequest request = emptyQueryParams();

        // when
        MessageResponses response = messageService.find(summer.getId(), request);
        MessageResponse foundMessage = response.getMessages().get(0);

        // then
        assertThat(foundMessage.isBookmarked()).isTrue();
    }

    @DisplayName("북마크가 등록 된 메시지라면, isBookmarked가 true 이다")
    @Test
    void isBookmarkedIsTrueIfBookmarked() {
        // given
        Member summer = members.save(summer());
        Channel notice = channels.save(notice());
        subscriptions.save(new ChannelSubscription(notice, summer, VIEW_ORDER_FIRST));

        Message message = messages.save(PLAIN_20220712_15_00_00.create(notice, summer));
        bookmarks.save(new Bookmark(summer, message));

        MessageRequest request = emptyQueryParams();

        // when
        MessageResponses response = messageService.find(summer.getId(), request);
        MessageResponse foundMessage = response.getMessages().get(0);

        // then
        assertThat(foundMessage.isBookmarked()).isTrue();
    }

    @DisplayName("리마인더가 등록되지 않은 메시지라면, isReminder가 false이고, remindDate값이 null이다")
    @Test
    void isReminderFalseAndNullRemindDateIfNotReminded() {
        // given
        Member summer = members.save(summer());
        Channel notice = channels.save(notice());
        subscriptions.save(new ChannelSubscription(notice, summer, VIEW_ORDER_FIRST));

        messages.save(PLAIN_20220712_15_00_00.create(notice, summer));

        MessageRequest request = emptyQueryParams();

        // when
        MessageResponses response = messageService.find(summer.getId(), request);
        MessageResponse foundMessage = response.getMessages().get(0);

        // then
        assertAll(
                () -> assertThat(foundMessage.isSetReminded()).isFalse(),
                () -> assertThat(foundMessage.getRemindDate()).isNull()
        );
    }

    @DisplayName("리마인더가 등록된 메시지라면, isReminder가 true고, remindDate값이 조회된다")
    @Test
    void isReminderTrueAndRemindDateIfReminded() {
        // given
        Member summer = members.save(summer());
        Channel notice = channels.save(notice());
        subscriptions.save(new ChannelSubscription(notice, summer, VIEW_ORDER_FIRST));

        Message message = messages.save(PLAIN_20220712_15_00_00.create(notice, summer));
        LocalDateTime remindDate = LocalDateTime.now().plusDays(1);
        reminders.save(new Reminder(summer, message, remindDate));

        MessageRequest request = emptyQueryParams();

        // when
        MessageResponses response = messageService.find(summer.getId(), request);
        MessageResponse foundMessage = response.getMessages().get(0);

        // then
        assertAll(
                () -> assertThat(foundMessage.isSetReminded()).isTrue(),
                () -> assertThat(foundMessage.getRemindDate()).isEqualTo(remindDate.toString())
        );
    }

    @DisplayName("메시지에 리마인더가 있지만 LocalDateTime.now()보다 오래됐다면, isSetReminded가 false다")
    @Test
    void messageSetRemindedPastIsSetRemindedFalse() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-13T00:00:00Z"));
        LocalDateTime pastFromMockTime = LocalDateTime.parse("2022-08-12T00:00:00");

        Member summer = members.save(summer());
        Channel notice = channels.save(notice());
        subscriptions.save(new ChannelSubscription(notice, summer, VIEW_ORDER_FIRST));

        Message message = messages.save(PLAIN_20220712_15_00_00.create(notice, summer));
        reminders.save(new Reminder(summer, message, pastFromMockTime));

        MessageRequest request = emptyQueryParams();

        // when
        MessageResponses response = messageService.find(summer.getId(), request);
        MessageResponse foundMessage = response.getMessages().get(0);

        // then
        assertThat(foundMessage.isSetReminded()).isFalse();
    }

    @DisplayName("메시지에 리마인더가 있지만 LocalDateTime.now()과 일치한다면, isSetReminded가 false다")
    @Test
    void messageSetRemindedSameAsNowIsSetRemindedFalse() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-13T00:00:00Z"));
        LocalDateTime sameAsMockTime = LocalDateTime.parse("2022-08-13T00:00:00");

        Member summer = members.save(summer());
        Channel notice = channels.save(notice());
        subscriptions.save(new ChannelSubscription(notice, summer, VIEW_ORDER_FIRST));

        Message message = messages.save(PLAIN_20220712_15_00_00.create(notice, summer));
        reminders.save(new Reminder(summer, message, sameAsMockTime));

        MessageRequest request = emptyQueryParams();

        // when
        MessageResponses response = messageService.find(summer.getId(), request);
        MessageResponse foundMessage = response.getMessages().get(0);

        // then
        assertThat(foundMessage.isSetReminded()).isFalse();
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

    private List<Long> extractIds(final List<Message> messages) {
        return messages.stream()
                .filter(message -> !message.getText().isEmpty())
                .map(Message::getId)
                .collect(Collectors.toList());
    }

    private List<Long> extractDateOrderedIds(final List<Message> messages, final int count) {
        List<Long> dateOrderedMessageIds = messages.stream()
                .filter(message -> !message.getText().isEmpty())
                .sorted(Comparator.comparing(Message::getPostedDate).reversed())
                .map(Message::getId)
                .collect(Collectors.toList());

        return dateOrderedMessageIds.subList(0, count);
    }

    private List<Long> extractIdsCreatedBeforeTarget(final List<Message> messages,
                                                     final Message target,
                                                     final int count) {
        List<Long> dateOrderedIdsBeforeTarget = messages.stream()
                .filter(message -> !message.getText().isEmpty()
                        && message.getPostedDate().isBefore(target.getPostedDate()))
                .sorted(Comparator.comparing(Message::getPostedDate).reversed())
                .map(Message::getId)
                .collect(Collectors.toList());

        return dateOrderedIdsBeforeTarget.subList(0, count);
    }

    private List<Long> extractIdsCreatedAfterTarget(final List<Message> messages,
                                                    final Message target,
                                                    final int count) {
        List<Long> reverseOrderedIdsAfterTarget = messages.stream()
                .filter(message -> !message.getText().isEmpty()
                        && message.getPostedDate().isAfter(target.getPostedDate()))
                .sorted(Comparator.comparing(Message::getPostedDate))
                .map(Message::getId)
                .collect(Collectors.toList());

        List<Long> idsSlicedByCount = reverseOrderedIdsAfterTarget.subList(0, count);
        Collections.reverse(idsSlicedByCount);

        return idsSlicedByCount;
    }
}
