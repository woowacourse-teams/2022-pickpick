package com.pickpick.message.domain;

import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.MemberFixture.HOPE;
import static com.pickpick.fixture.MessageFixtures.PLAIN_20220715_17_00_00;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pickpick.channel.domain.Channel;
import com.pickpick.exception.message.ReminderInvalidDateException;
import com.pickpick.member.domain.Member;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReminderTest {

    @DisplayName("리마인드 시각을 현재 시각보다 과거 시각으로 생성 시 예외 발생")
    @Test
    void remindDateForPastThrowsException() {
        Member hope = HOPE.create();
        Channel notice = NOTICE.create();
        Message message = PLAIN_20220715_17_00_00.create(notice, hope);

        LocalDateTime past = LocalDateTime.now().minusSeconds(1);
        assertThatThrownBy(() -> new Reminder(hope, message, past))
                .isInstanceOf(ReminderInvalidDateException.class);
    }

    @DisplayName("현재 시각보다 과거 시각으로 리마인드 시각 수정 시 예외 발생")
    @Test
    void updateRemindDateForPastThrowsException() {
        Member hope = HOPE.create();
        Channel notice = NOTICE.create();
        Message message = PLAIN_20220715_17_00_00.create(notice, hope);

        LocalDateTime future = LocalDateTime.now().plusHours(1);
        Reminder reminder = new Reminder(hope, message, future);

        LocalDateTime past = LocalDateTime.now().minusSeconds(1);
        assertThatThrownBy(() -> reminder.updateRemindDate(past))
                .isInstanceOf(ReminderInvalidDateException.class);
    }
}
