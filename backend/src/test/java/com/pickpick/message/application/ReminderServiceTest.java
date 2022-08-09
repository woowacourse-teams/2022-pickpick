package com.pickpick.message.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.message.ui.dto.ReminderResponse;
import com.pickpick.message.ui.dto.ReminderResponses;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql({"/truncate.sql", "/reminder.sql"})
@Transactional
@SpringBootTest
class ReminderServiceTest {

    @Autowired
    private ReminderService reminderService;

    private static Stream<Arguments> parameterProvider() {
        return Stream.of(
                Arguments.arguments("멤버 ID 2번으로 리마인더를 조회한다", null, 2L, List.of(1L), true),
                Arguments.arguments("멤버 ID가 1번이고 리마인더 id 10번일 때 리마인더 목록을 조회한다", 10L, 1L,
                        List.of(11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L, 23L), true),
                Arguments.arguments("리마인더 조회 시 가장 최신인 리마인더가 포함된다면 isLast가 true이다", null, 2L, List.of(1L), true),
                Arguments.arguments("리마인더 조회 시 가장 최신인 리마인더가 포함되지 않는다면 isLast가 false이다", 2L, 1L,
                        List.of(3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L,
                                22L), false)
        );
    }

    @DisplayName("리마인더 조회")
    @ParameterizedTest(name = "{0}")
    @MethodSource("parameterProvider")
    void findBookmarks(final String subscription, final Long reminderId, final Long memberId,
                       final List<Long> expectedIds, final boolean expectedIsLast) {
        System.out.println(LocalDateTime.now());
        // given & when
        ReminderResponses response = reminderService.find(reminderId, memberId);

        // then
        List<Long> ids = convertToIds(response);
        assertAll(
                () -> assertThat(ids).containsExactlyElementsOf(expectedIds),
                () -> assertThat(response.isLast()).isEqualTo(expectedIsLast)
        );
    }

    private List<Long> convertToIds(final ReminderResponses response) {
        return response.getReminders()
                .stream()
                .map(ReminderResponse::getId)
                .collect(Collectors.toList());
    }
}
