package com.pickpick.message.application;

import static com.pickpick.fixture.MessageRequestFactory.emptyQueryParams;
import static com.pickpick.fixture.MessageRequestFactory.emptyQueryParamsWithCount;
import static com.pickpick.fixture.MessageRequestFactory.fromLatestInChannelIds;
import static com.pickpick.fixture.MessageRequestFactory.futureFromTargetMessageInChannelIds;
import static com.pickpick.fixture.MessageRequestFactory.pastFromTargetMessageInChannelIds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.fixture.MessageRequestFactory;
import com.pickpick.message.ui.dto.MessageRequest;
import com.pickpick.message.ui.dto.MessageResponse;
import com.pickpick.message.ui.dto.MessageResponses;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql({"/truncate.sql", "/message_fixture.sql"})
@Transactional
@SpringBootTest
class MessageServiceQueryTestSql {

    private static final int MESSAGE_COUNT = 5;
    private static final int MESSAGE_COUNT_DEFAULT = 20;
    private static final String MESSAGE_KEYWORD = "줍줍";
    private static final long MEMBER_ID = 1L;
    private static final int ALL_MESSAGES_COUNT = 50;
    private static final long CHANNEL_NOTICE_ID = 1L;
    private static final long CHANNEL_FREECHAT_ID = 2L;
    private static final int KEYWORD_MESSAGES_COUNT = 20;
    private static final long TARGET_MESSAGE_ID = 10L;

    @Autowired
    private MessageService messageService;

    private static List<Long> createExpectedMessageIds(final long startInclusive, final long endInclusive) {
        return LongStream.rangeClosed(endInclusive, startInclusive)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    @DisplayName("쿼리 파라미터와 count가 없다면, 회원의 첫번째 순서 구독 채널의 최신 메시지를 20개 내림차순 조회한다")
    @Test
    void findMessagesEmptyParameters() {
        // given
        MessageRequest request = emptyQueryParams();

        // when
        MessageResponses response = messageService.find(MEMBER_ID, request);

        // then
        List<MessageResponse> foundMessages = response.getMessages();
        List<Long> expectedIds = createExpectedMessageIds(20L, 1L);

        assertAll(
                () -> assertThat(foundMessages).extracting("id").isEqualTo(expectedIds),
                () -> assertThat(foundMessages).hasSize(MESSAGE_COUNT_DEFAULT)
        );
    }

    @DisplayName("쿼리 파라미터가 없고 count가 있다면, 회원의 첫번째 순서 구독 채널의 최신 메시지를 count개 내림차순 조회한다")
    @Test
    void findMessagesEmptyParametersWithCount() {
        // given
        MessageRequest request = emptyQueryParamsWithCount(MESSAGE_COUNT);

        // when
        MessageResponses response = messageService.find(MEMBER_ID, request);

        // then
        List<MessageResponse> foundMessages = response.getMessages();
        List<Long> expectedIds = createExpectedMessageIds(20L, 16L);

        assertAll(
                () -> assertThat(foundMessages).hasSize(MESSAGE_COUNT),
                () -> assertThat(foundMessages).extracting("id").isEqualTo(expectedIds)
        );
    }

    @DisplayName("더 이상 조회할 메시지가 없다면 isLast로 true를 반환한다")
    @Test
    void findMessagesIsLast() {
        // given
        MessageRequest request = emptyQueryParamsWithCount(ALL_MESSAGES_COUNT);

        // when
        MessageResponses response = messageService.find(MEMBER_ID, request);
        boolean isLast = response.isLast();

        // then
        assertThat(isLast).isEqualTo(true);
    }

    @DisplayName("메시지 조회 시, 텍스트가 비어있는 메시지는 필터링된다")
    @Test
    void emptyMessagesShouldBeFiltered() {
        // given
        MessageRequest request = fromLatestInChannelIds(List.of(CHANNEL_NOTICE_ID), ALL_MESSAGES_COUNT);

        // when
        MessageResponses messageResponses = messageService.find(MEMBER_ID, request);

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
        MessageRequest request = MessageRequestFactory.searchByKeywordInChannelIds(
                List.of(CHANNEL_NOTICE_ID, CHANNEL_FREECHAT_ID), MESSAGE_KEYWORD, ALL_MESSAGES_COUNT);

        // when
        MessageResponses response = messageService.find(MEMBER_ID, request);

        // then
        List<MessageResponse> foundMessages = response.getMessages();
        boolean isContainingKeyword = foundMessages.stream()
                .allMatch(message -> message.getText().contains(MESSAGE_KEYWORD));

        assertAll(
                () -> assertThat(foundMessages).hasSize(KEYWORD_MESSAGES_COUNT),
                () -> assertThat(isContainingKeyword).isTrue()
        );
    }

    @DisplayName("채널과 메시지ID로 앞선 메시지 조회 시, 메시지 기준 해당 채널의 미래 메시지를 시간 내림차순 조회한다")
    @Test
    void findMessagesWithChannelAndMessageNotNeedPast() {
        // given
        MessageRequest request = futureFromTargetMessageInChannelIds(List.of(CHANNEL_NOTICE_ID), TARGET_MESSAGE_ID,
                MESSAGE_COUNT);

        // when
        MessageResponses response = messageService.find(MEMBER_ID, request);

        // then
        List<MessageResponse> foundMessages = response.getMessages();
        List<Long> expectedIds = createExpectedMessageIds(15L, 11L);

        assertThat(foundMessages).extracting("id").containsExactlyElementsOf(expectedIds);
    }

    @DisplayName("채널과 메시지ID로 지난 메시지 조회 시, 메시지 기준 해당 채널의 과거 메시지를 시간 내림차순 조회한다")
    @Test
    void findMessagesWithChannelAndMessageNeedPast() {
        // given
        MessageRequest request = pastFromTargetMessageInChannelIds(List.of(CHANNEL_NOTICE_ID), TARGET_MESSAGE_ID,
                MESSAGE_COUNT);

        // when
        MessageResponses response = messageService.find(MEMBER_ID, request);

        // then
        List<MessageResponse> foundMessages = response.getMessages();
        List<Long> expectedIds = createExpectedMessageIds(9L, 5L);

        assertThat(foundMessages).extracting("id").containsExactlyElementsOf(expectedIds);
    }
}
