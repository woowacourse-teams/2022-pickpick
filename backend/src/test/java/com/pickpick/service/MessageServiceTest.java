package com.pickpick.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.message.application.MessageService;
import com.pickpick.message.ui.dto.MessageRequest;
import com.pickpick.message.ui.dto.MessageResponse;
import com.pickpick.message.ui.dto.MessageResponses;
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
            final String description, final MessageRequest messageRequest,
            final List<Long> expectedMessageIds, final boolean expectedLast) {
        // given
        MessageResponses messageResponses = messageService.find(messageRequest);

        // when
        List<MessageResponse> messages = messageResponses.getMessages();
        boolean last = messageResponses.isLast();

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

    private static MessageRequest getSlackMessageRequest() {
        return new MessageRequest("", "", List.of(5L), false, 1L, 7);
    }
}
