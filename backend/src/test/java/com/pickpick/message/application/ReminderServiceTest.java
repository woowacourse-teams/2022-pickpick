package com.pickpick.message.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.message.ReminderDeleteFailureException;
import com.pickpick.exception.message.ReminderNotFoundException;
import com.pickpick.exception.message.ReminderUpdateFailureException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.domain.Reminder;
import com.pickpick.message.domain.ReminderRepository;
import com.pickpick.message.ui.dto.ReminderFindRequest;
import com.pickpick.message.ui.dto.ReminderResponse;
import com.pickpick.message.ui.dto.ReminderResponses;
import com.pickpick.message.ui.dto.ReminderSaveRequest;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/truncate.sql", "/reminder.sql"})
@Transactional
@SpringBootTest
class ReminderServiceTest {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private MemberRepository members;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private ReminderRepository reminders;

    @SpyBean
    private Clock clock;

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

    @DisplayName("리마인더를 생성한다")
    @Sql("/truncate.sql")
    @Test
    void save() {
        // given
        Member member = members.save(new Member("U1234", "사용자", "user.png"));
        Channel channel = channels.save(new Channel("C1234", "기본채널"));
        Message message = messages.save(
                new Message("M1234", "메시지", member, channel, LocalDateTime.now(), LocalDateTime.now()));

        ReminderSaveRequest request = new ReminderSaveRequest(message.getId(), LocalDateTime.now().plusDays(1));
        int beforeSize = findReminderSize(member);

        // when
        reminderService.save(member.getId(), request);

        // then
        int afterSize = findReminderSize(member);
        assertThat(beforeSize + 1).isEqualTo(afterSize);
    }

    private int findReminderSize(final Member member) {
        return reminderService.find(new ReminderFindRequest(null, null), member.getId()).getReminders().size();
    }

    @DisplayName("리마인더 단건 조회")
    @Test
    void findOneReminder() {
        // given
        Long memberId = 2L;
        Long messageId = 1L;

        // when
        ReminderResponse reminder = reminderService.findOne(messageId, memberId);

        // then
        assertAll(
                () -> assertThat(reminder.getId()).isEqualTo(1L),
                () -> assertThat(reminder.getRemindDate()).isEqualTo(LocalDateTime.of(2022, 8, 12, 14, 20))
        );
    }

    @DisplayName("리마인더가 존재하지 않는 메시지를 단건 조회할 경우 예외 발생")
    @Test
    void findNotExistOneThenThrowException() {
        // given
        Long memberId = 2L;
        Long messageId = 20L;

        // when & then
        assertThatThrownBy(() -> reminderService.findOne(messageId, memberId))
                .isInstanceOf(ReminderNotFoundException.class);
    }

    @DisplayName("리마인더 목록 조회")
    @ParameterizedTest(name = "{0}")
    @MethodSource("parameterProvider")
    void findReminders(final String subscription, final Long reminderId, final Long memberId,
                       final List<Long> expectedIds, final boolean expectedIsLast) {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        // when
        ReminderResponses response = reminderService.find(new ReminderFindRequest(reminderId, null), memberId);

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

    @DisplayName("리마인더 조회 시 count가 없으면 default 값을 20으로 세팅")
    @Test
    void findRemindersByDefaultCount() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        // when
        ReminderResponses response = reminderService.find(new ReminderFindRequest(null, null), 1L);

        // then
        int size = response.getReminders().size();
        assertThat(size).isEqualTo(20);
    }

    @DisplayName("리마인더 조회 시 count 값이 10이면 10개 조회")
    @Test
    void findRemindersByCount() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));
        int count = 10;

        // when
        ReminderResponses response = reminderService.find(new ReminderFindRequest(null, count), 1L);

        // then
        int size = response.getReminders().size();
        assertThat(size).isEqualTo(count);
    }

    @DisplayName("리마인더 조회 해당 날의 reminder개수와 count가 동일한 경우 미래의 리마인더가 존재하면 isLast가 false 이다.")
    @Test
    void findSameDayReminder() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2023-07-07T15:20:00Z"));
        int count = 2;

        // when
        ReminderResponses response = reminderService.find(new ReminderFindRequest(null, count), 3L);

        // then
        int size = response.getReminders().size();
        assertAll(
                () -> assertThat(size).isEqualTo(count),
                () -> assertThat(response.isLast()).isFalse(),
                () -> assertThat(response.getReminders()).extracting("id")
                        .containsExactly(29L, 30L)
        );
    }

    @DisplayName("리마인더 조회 해당 날의 reminder의 개수보다 적은 COUNT로 조회할 경우 isLast가 false이다.")
    @Test
    void findSameDayReminderByCount() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2023-08-07T15:20:00Z"));
        int count = 2;

        // when
        ReminderResponses response = reminderService.find(new ReminderFindRequest(null, count), 3L);

        // then
        int size = response.getReminders().size();
        assertAll(
                () -> assertThat(size).isEqualTo(count),
                () -> assertThat(response.isLast()).isFalse(),
                () -> assertThat(response.getReminders()).extracting("id")
                        .containsExactly(25L, 26L)
        );
    }

    @DisplayName("리마인더 조회 해당 날의 reminder개수와 count가 동일한 경우 미래의 리마인더가 존재하지 않으면 isLast가 true 이다.")
    @Test
    void findSameDayReminderByCountAndReminderId() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2023-08-07T15:20:00Z"));
        int count = 2;

        // when
        ReminderResponses response = reminderService.find(new ReminderFindRequest(26L, count), 3L);

        // then
        int size = response.getReminders().size();
        assertAll(
                () -> assertThat(size).isEqualTo(count),
                () -> assertThat(response.isLast()).isTrue(),
                () -> assertThat(response.getReminders()).extracting("id")
                        .containsExactly(27L, 28L)
        );
    }

    @DisplayName("리마인더가 날짜 + ID 순으로 올바르게 조회되는지 확인한다")
    @Test
    void findRemindersOrderByDateAndId() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2023-06-07T15:20:00Z"));
        int count = 7;

        // when
        ReminderResponses response = reminderService.find(new ReminderFindRequest(null, count), 3L);

        // then
        int size = response.getReminders().size();
        assertAll(
                () -> assertThat(size).isEqualTo(count),
                () -> assertThat(response.isLast()).isTrue(),
                () -> assertThat(response.getReminders()).extracting("id")
                        .containsExactly(31L, 29L, 30L, 25L, 26L, 27L, 28L)
        );
    }


    @DisplayName("오늘 날짜보다 더 오래된 날짜에 리마인드한 내역은 조회되지 않는다.")
    @Test
    void findWithoutOldRemindDate() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        // when
        ReminderResponses response = reminderService.find(new ReminderFindRequest(null, null), 1L);

        // then
        List<Long> ids = convertToIds(response);
        assertAll(
                () -> assertThat(ids).doesNotContainAnyElementsOf(List.of(24L)),
                () -> assertThat(response.isLast()).isFalse()
        );
    }

    @DisplayName("리마인더 수정")
    @Test
    void update() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        LocalDateTime updateTime = LocalDateTime.now(clock).plusDays(1);
        long memberId = 1L;
        long messageId = 2L;

        // when
        reminderService.update(memberId, new ReminderSaveRequest(messageId, updateTime));

        // then
        Optional<Reminder> expected = reminders.findByMessageIdAndMemberId(messageId, memberId);

        assertAll(
                () -> assertThat(expected).isPresent(),
                () -> assertThat(expected.get().getRemindDate()).isEqualTo(updateTime)
        );
    }

    @DisplayName("다른 사용자의 리마인더 수정시 예외")
    @Test
    void updateOtherMembers() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        ReminderSaveRequest request = new ReminderSaveRequest(1L, LocalDateTime.now(clock).plusDays(1));

        // when & then
        assertThatThrownBy(() -> reminderService.update(1L, request))
                .isInstanceOf(ReminderUpdateFailureException.class);
    }

    @DisplayName("리마인더 삭제")
    @Test
    void delete() {
        // given & when
        reminderService.delete(1L, 2L);

        // then
        Optional<Reminder> actual = reminders.findById(1L);
        assertThat(actual).isEmpty();
    }

    @DisplayName("다른 사용자의 리마인더 삭제시 예외")
    @Test
    void deleteOtherMembers() {
        // given & when & then
        assertThatThrownBy(() -> reminderService.delete(1L, 1L))
                .isInstanceOf(ReminderDeleteFailureException.class);
    }
}
