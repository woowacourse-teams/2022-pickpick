package com.pickpick.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.controller.dto.SlackMessageRequest;
import com.pickpick.controller.dto.SlackMessageResponse;
import com.pickpick.controller.dto.SlackMessageResponses;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql({"/truncate.sql", "/message.sql"})
@TestConstructor(autowireMode = AutowireMode.ALL)
@Transactional
@SpringBootTest
class MessageServiceTest {

    private final MessageService messageService;

    public MessageServiceTest(final MessageService messageService) {
        this.messageService = messageService;
    }

    @DisplayName("메시지 조회 요청에 따른 메시지가 응답된다")
    @MethodSource("slackMessageRequest")
    @ParameterizedTest(name = "{0}")
    void findMessages(
            final String description, final SlackMessageRequest slackMessageRequest,
            final List<Long> expectedMessageIds, final boolean expectedLast) {
        // given
        SlackMessageResponses slackMessageResponses = messageService.find(slackMessageRequest);

        // when
        List<SlackMessageResponse> messages = slackMessageResponses.getMessages();
        boolean last = slackMessageResponses.isLast();

        // then
        assertAll(
                () -> assertThat(messages).extracting("id").isEqualTo(expectedMessageIds),
                () -> assertThat(last).isEqualTo(expectedLast)
        );
    }

    private static Stream<Arguments> slackMessageRequest() {
        return Stream.of(
                Arguments.of(
                        "5번 채널에서 메시지ID가 1인 메시지 이후에 작성된 메시지 7개 조회",
                        getSlackMessageRequest(),
                        List.of(8L, 7L, 6L, 5L, 4L, 3L, 2L),
                        false)
        );
    }

    private static SlackMessageRequest getSlackMessageRequest() {
        SlackMessageRequest slackMessageRequest = new SlackMessageRequest();
        slackMessageRequest.setChannelIds(List.of(5L));
        slackMessageRequest.setNeedPastMessage(false);
        slackMessageRequest.setMessageId(1L);
        slackMessageRequest.setMessageCount(7);

        return slackMessageRequest;
    }
}
