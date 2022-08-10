package com.pickpick.message.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.config.QuerydslConfig;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.ui.dto.ReminderRequest;
import com.pickpick.message.ui.dto.ReminderResponse;
import com.pickpick.message.ui.dto.ReminderResponses;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/truncate.sql", "/reminder.sql"})
@Import(value = {ReminderService.class, QuerydslConfig.class})
@DataJpaTest
class ReminderServiceTest {

    private static MockedStatic<LocalDateTime> localDateTimeMockedStatic;
    @Autowired
    private ReminderService reminderService;

    @Autowired
    private MemberRepository members;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private ChannelRepository channels;

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

    @BeforeAll
    static void setMock() {
        localDateTimeMockedStatic = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
        LocalDateTime currentLocalDate = LocalDateTime.of(2022, 8, 10, 0, 0, 0);
        localDateTimeMockedStatic.when(LocalDateTime::now).thenReturn(currentLocalDate);
    }

    @AfterAll
    static void closeMock() {
        localDateTimeMockedStatic.close();
    }

    @DisplayName("리마인더를 생성한다")
    @Sql("/truncate.sql")
    @Test
    void save() {
        // given
        Member member = new Member("U1234", "사용자", "user.png");
        members.save(member);
        Channel channel = new Channel("C1234", "기본채널");
        channels.save(channel);
        Message message = new Message("M1234", "메시지", member, channel, LocalDateTime.now(), LocalDateTime.now());
        messages.save(message);

        ReminderRequest reminderRequest = new ReminderRequest(message.getId(), LocalDateTime.now().plusDays(1));
        int beforeSize = findReminderSize(member);

        // when
        reminderService.save(member.getId(), reminderRequest);

        // then
        int afterSize = findReminderSize(member);
        assertThat(beforeSize + 1).isEqualTo(afterSize);
    }

    private int findReminderSize(final Member member) {
        return reminderService.find(null, member.getId()).getReminders().size();
    }

    @DisplayName("리마인더 조회")
    @ParameterizedTest(name = "{0}")
    @MethodSource("parameterProvider")
    void findReminders(final String subscription, final Long reminderId, final Long memberId,
                       final List<Long> expectedIds, final boolean expectedIsLast) {
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

    @DisplayName("오늘 날짜보다 더 오래된 날짜에 리마인드한 내역은 조회되지 않는다.")
    @Test
    void findWithoutOldRemindDate() {
        // given & when
        ReminderResponses response = reminderService.find(null, 1L);

        // then
        List<Long> ids = convertToIds(response);
        assertAll(
                () -> assertThat(ids).doesNotContainAnyElementsOf(List.of(24L)),
                () -> assertThat(response.isLast()).isFalse()
        );
    }
}
