package com.pickpick.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.message.application.MessageService;
import com.pickpick.message.ui.dto.MessageRequest;
import com.pickpick.message.ui.dto.MessageResponse;
import com.pickpick.message.ui.dto.MessageResponses;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Sql({"/truncate.sql", "/message.sql"})
@TestConstructor(autowireMode = AutowireMode.ALL)
@Transactional
@SpringBootTest
class MessageServiceTest {

    private static final long MEMBER_ID = 1L;

    private final MessageService messageService;

    public MessageServiceTest(final MessageService messageService) {
        this.messageService = messageService;
    }

    private static Stream<Arguments> slackMessageRequest() {
        return Stream.of(
                Arguments.of(
                        "5번 채널에서 메시지ID가 1인 메시지 이후에 작성된 메시지 7개 조회",
                        new MessageRequest("", "", List.of(5L), false, 1L, 7),
                        createExpectedMessageIds(8L, 2L),
                        false),
                Arguments.of(
                        "쿼리 파라미터가 전혀 전달되지 않았을 경우, 회원의 채널 정렬 상 첫번째 채널의 최신 20개 메시지를 작성시간 내림차순으로 응답해야 한다.",
                        new MessageRequest("", "", Collections.emptyList(), true, null, 20),
                        createExpectedMessageIds(38L, 19L),
                        false),
                Arguments.of(
                        "쿼리 파라미터가 전혀 전달되지 않았을 경우, 회원의 채널 정렬 상 첫번째 채널의 최신 20개 메시지를 작성시간 내림차순으로 응답해야 한다.",
                        new MessageRequest("", "", null, true, null, 20),
                        createExpectedMessageIds(38L, 19L),
                        false)
        );
    }

    private static List<Long> createExpectedMessageIds(final long startInclusive, final long endInclusive) {
        return LongStream.rangeClosed(endInclusive, startInclusive)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    @DisplayName("메시지 조회 요청에 따른 메시지가 응답된다")
    @MethodSource("slackMessageRequest")
    @ParameterizedTest(name = "{0}")
    void findMessages(
            final String description, final MessageRequest messageRequest,
            final List<Long> expectedMessageIds, final boolean expectedLast) {
        // given
        MessageResponses messageResponses = messageService.find(MEMBER_ID, messageRequest);

        // when
        List<MessageResponse> messages = messageResponses.getMessages();
        boolean last = messageResponses.isLast();

        // then
        assertAll(
                () -> assertThat(messages).extracting("id").isEqualTo(expectedMessageIds),
                () -> assertThat(last).isEqualTo(expectedLast)
        );
    }

    @DisplayName("메시지 조회 시, 텍스트가 비어있는 메시지는 필터링된다")
    @Test
    void emptyMessagesShouldBeFiltered() {
        // given
        MessageRequest messageRequest = new MessageRequest("", "", List.of(5L), true, null, 200);

        // when
        MessageResponses messageResponses = messageService.find(MEMBER_ID, messageRequest);
        List<MessageResponse> messages = messageResponses.getMessages();
        boolean hasEmptyMessageResponse = messages.stream()
                .anyMatch(message -> !StringUtils.hasText(message.getText()));

        // then
        assertThat(hasEmptyMessageResponse).isFalse();
    }
}
