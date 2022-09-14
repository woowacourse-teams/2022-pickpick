package com.pickpick.message.application;

import com.pickpick.exception.member.MemberNotFoundException;
import com.pickpick.exception.message.MessageNotFoundException;
import com.pickpick.exception.message.ReminderDeleteFailureException;
import com.pickpick.exception.message.ReminderNotFoundException;
import com.pickpick.exception.message.ReminderUpdateFailureException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.domain.QReminder;
import com.pickpick.message.domain.Reminder;
import com.pickpick.message.domain.ReminderRepository;
import com.pickpick.message.ui.dto.ReminderFindRequest;
import com.pickpick.message.ui.dto.ReminderResponse;
import com.pickpick.message.ui.dto.ReminderResponses;
import com.pickpick.message.ui.dto.ReminderSaveRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ReminderService {

    private final ReminderRepository reminders;
    private final MemberRepository members;
    private final MessageRepository messages;
    private final JPAQueryFactory jpaQueryFactory;
    private final Clock clock;

    public ReminderService(final ReminderRepository reminders, final MemberRepository members,
                           final MessageRepository messages, final JPAQueryFactory jpaQueryFactory, final Clock clock) {
        this.reminders = reminders;
        this.members = members;
        this.messages = messages;
        this.jpaQueryFactory = jpaQueryFactory;
        this.clock = clock;
    }

    @Transactional
    public void save(final Long memberId, final ReminderSaveRequest request) {
        Member member = members.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Message message = messages.findById(request.getMessageId())
                .orElseThrow(() -> new MessageNotFoundException(request.getMessageId()));

        Reminder reminder = new Reminder(member, message, request.getReminderDate());
        reminders.save(reminder);
    }

    public ReminderResponse findOne(final Long messageId, final Long memberId) {
        Reminder reminder = reminders.findByMessageIdAndMemberId(messageId, memberId)
                .orElseThrow(() -> new ReminderNotFoundException(messageId, memberId));

        return ReminderResponse.from(reminder);
    }

    public ReminderResponses find(final ReminderFindRequest request, final Long memberId) {
        List<Reminder> reminderList = findReminders(request, memberId);

        return new ReminderResponses(toReminderResponseList(reminderList), isLast(reminderList, memberId));
    }

    private List<Reminder> findReminders(final ReminderFindRequest request, final Long memberId) {
        return jpaQueryFactory
                .selectFrom(QReminder.reminder)
                .leftJoin(QReminder.reminder.message)
                .fetchJoin()
                .where(QReminder.reminder.member.id.eq(memberId))
                .where(remindDateCondition(request.getReminderId()))
                .orderBy(QReminder.reminder.remindDate.asc(), QReminder.reminder.id.asc())
                .limit(request.getCount())
                .fetch();
    }

    private List<ReminderResponse> toReminderResponseList(final List<Reminder> foundReminders) {
        return foundReminders.stream()
                .map(ReminderResponse::from)
                .collect(Collectors.toList());
    }

    private BooleanExpression remindDateCondition(final Long reminderId) {
        if (Objects.isNull(reminderId)) {
            return QReminder.reminder.remindDate.after(LocalDateTime.now(clock));
        }

        Reminder reminder = reminders.findById(reminderId)
                .orElseThrow(() -> new ReminderNotFoundException(reminderId));

        if (isTargetDateMessageLeft(reminder)) {
            return (QReminder.reminder.remindDate.eq(reminder.getRemindDate())
                    .and(QReminder.reminder.id.gt(reminderId)))
                    .or(QReminder.reminder.remindDate.after(reminder.getRemindDate()));
        }

        return QReminder.reminder.remindDate.after(reminder.getRemindDate());
    }

    private boolean isTargetDateMessageLeft(final Reminder reminder) {
        Optional<Long> max = reminders.findAllByRemindDate(reminder.getRemindDate())
                .stream()
                .map(Reminder::getId)
                .max(Long::compareTo);

        return max.isPresent() && max.get() > reminder.getId();
    }

    private boolean isLast(final List<Reminder> reminderList, final Long memberId) {
        if (reminderList.isEmpty()) {
            return true;
        }

        if (isTargetDateMessageLeft(reminderList)) {
            return false;
        }

        Integer result = jpaQueryFactory
                .selectOne()
                .from(QReminder.reminder)
                .where(QReminder.reminder.member.id.eq(memberId))
                .where(meetIsLastCondition(reminderList))
                .fetchFirst();

        return Objects.isNull(result);
    }

    private boolean isTargetDateMessageLeft(final List<Reminder> reminderList) {
        Reminder targetReminder = reminderList.get(reminderList.size() - 1);
        LocalDateTime remindDate = targetReminder.getRemindDate();
        Optional<Long> reminderId = reminders.findAllByRemindDate(remindDate)
                .stream()
                .map(Reminder::getId)
                .filter(id -> id > targetReminder.getId())
                .findFirst();

        return reminderId.isPresent();
    }

    private BooleanExpression meetIsLastCondition(final List<Reminder> reminderList) {
        Reminder targetReminder = reminderList.get(reminderList.size() - 1);

        LocalDateTime remindDate = targetReminder.getRemindDate();

        return QReminder.reminder.remindDate.after(remindDate);
    }

    @Transactional
    public void update(final Long memberId, final ReminderSaveRequest request) {
        Reminder reminder = reminders.findByMessageIdAndMemberId(request.getMessageId(), memberId)
                .orElseThrow(() -> new ReminderUpdateFailureException(request.getMessageId(), memberId));

        reminder.updateRemindDate(request.getReminderDate());
    }

    @Transactional
    public void delete(final Long messageId, final Long memberId) {
        Reminder reminder = reminders.findByMessageIdAndMemberId(messageId, memberId)
                .orElseThrow(() -> new ReminderDeleteFailureException(messageId, memberId));

        reminders.deleteById(reminder.getId());
    }
}
