package com.pickpick.message.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.pickpick.config.DatabaseCleaner;
import com.pickpick.message.ui.dto.MessageRequest;
import com.pickpick.message.ui.dto.MessageResponse;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/message.sql"})
@SpringBootTest
class MessageServiceTest {

    private static final long MEMBER_ID = 1L;

    @Autowired
    private MessageService messageService;
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @SpyBean
    private Clock clock;

    private static Stream<Arguments> messageRequestWithReminder() {
        return Stream.of(
                Arguments.of("현재 시간보다 오래된 리마인더가 존재하면 isSetReminded가 false이다",
                        "2022-08-13T00:00:00Z",
                        new MessageRequest("", "", List.of(5L), true, null, 1),
                        false),
                Arguments.of("현재 시간보다 최신인 리마인더가 존재하면 isSetReminded가 true이다",
                        "2022-08-10T00:00:00Z",
                        new MessageRequest("", "", List.of(5L), true, null, 1),
                        true),
                Arguments.of("현재 시간과 동일한 리마인더가 존재하면 isSetReminded가 false이다",
                        "2022-08-12T14:20:00Z",
                        new MessageRequest("", "", List.of(5L), true, null, 1),
                        false)
        );
    }

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("메시지 조회 시 리마인더 여부 함께 조회된다")
    @MethodSource("messageRequestWithReminder")
    @ParameterizedTest(name = "{0}")
    void findSetRemindedMessage(final String description, final String nowDate, final MessageRequest messageRequest,
                                final boolean expected) {
        // given
        given(clock.instant())
                .willReturn(Instant.parse(nowDate));

        // when
        MessageResponse message = messageService.find(MEMBER_ID, messageRequest)
                .getMessages()
                .get(0);

        // then
        assertThat(message.isSetReminded()).isEqualTo(expected);
    }

    @DisplayName("메시지 조회 시, remindDate가 함께 전달된다")
    @Test
    void checkRemindDate2() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        MessageRequest request = new MessageRequest("", "", List.of(5L), true, null, 1);

        // when
        MessageResponse message = messageService.find(MEMBER_ID, request)
                .getMessages()
                .get(0);

        // then
        assertThat(message.getRemindDate()).isEqualTo(LocalDateTime.of(2022, 8, 12, 14, 20, 0));
    }

    @DisplayName("메시지 조회 시, remindDate 값이 없으면 빈 값으로 전달된다")
    @Test
    void checkRemindDate() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-12T14:20:00Z"));

        MessageRequest request = new MessageRequest("", "", List.of(5L), true, null, 1);

        // when
        MessageResponse message = messageService.find(MEMBER_ID, request)
                .getMessages()
                .get(0);

        // then
        assertThat(message.getRemindDate()).isNull();
    }
}
