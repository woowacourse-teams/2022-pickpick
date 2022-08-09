package com.pickpick.message.application;

import com.pickpick.exception.message.ReminderNotFoundException;
import com.pickpick.message.domain.QReminder;
import com.pickpick.message.domain.Reminder;
import com.pickpick.message.domain.ReminderRepository;
import com.pickpick.message.ui.dto.ReminderResponse;
import com.pickpick.message.ui.dto.ReminderResponses;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ReminderService {

    public static final int COUNT = 20;

    private final ReminderRepository reminders;

    private final JPAQueryFactory jpaQueryFactory;

    public ReminderService(final ReminderRepository reminders, final JPAQueryFactory jpaQueryFactory) {
        this.reminders = reminders;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public ReminderResponses find(final Long reminderId, final Long memberId) {
        List<Reminder> reminderList = findReminders(reminderId, memberId);

        return new ReminderResponses(toReminderResponseList(reminderList), isLast(reminderList, memberId));
    }

    private List<Reminder> findReminders(final Long reminderId, final Long memberId) {
        return jpaQueryFactory
                .selectFrom(QReminder.reminder)
                .leftJoin(QReminder.reminder.message)
                .fetchJoin()
                .leftJoin(QReminder.reminder.member)
                .fetchJoin()
                .where(QReminder.reminder.member.id.eq(memberId))
                .where(remindDateCondition(reminderId))
                .orderBy(QReminder.reminder.remindDate.asc())
                .limit(COUNT)
                .fetch();
    }

    private List<ReminderResponse> toReminderResponseList(final List<Reminder> foundReminders) {
        return foundReminders.stream()
                .map(ReminderResponse::from)
                .collect(Collectors.toList());
    }

    private BooleanExpression remindDateCondition(final Long reminderId) {
        if (Objects.isNull(reminderId)) {
            return QReminder.reminder.remindDate.after(LocalDateTime.now());
        }

        Reminder reminder = reminders.findById(reminderId)
                .orElseThrow(() -> new ReminderNotFoundException(reminderId));

        return QReminder.reminder.remindDate.after(reminder.getRemindDate());
    }

    private boolean isLast(final List<Reminder> reminderList, final Long memberId) {
        if (reminderList.isEmpty()) {
            return true;
        }

        Integer result = jpaQueryFactory
                .selectOne()
                .from(QReminder.reminder)
                .where(QReminder.reminder.member.id.eq(memberId))
                .where(meetIsLastCondition(reminderList))
                .fetchFirst();

        return Objects.isNull(result);
    }

    private BooleanExpression meetIsLastCondition(final List<Reminder> reminderList) {
        Reminder targetReminder = reminderList.get(reminderList.size() - 1);

        LocalDateTime remindDate = targetReminder.getRemindDate();

        return QReminder.reminder.remindDate.after(remindDate);
    }
}
