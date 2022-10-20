package com.pickpick.message.application;

import com.pickpick.exception.message.ReminderDeleteFailureException;
import com.pickpick.exception.message.ReminderUpdateFailureException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.domain.QReminder;
import com.pickpick.message.domain.Reminder;
import com.pickpick.message.domain.ReminderRepository;
import com.pickpick.message.support.SlackIdExtractor;
import com.pickpick.message.ui.dto.ReminderFindRequest;
import com.pickpick.message.ui.dto.ReminderResponse;
import com.pickpick.message.ui.dto.ReminderResponses;
import com.pickpick.message.ui.dto.ReminderSaveRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ReminderService {

    private static final String MENTION_PREFIX = "<@";
    private static final String MENTION_SUFFIX = ">";

    private final ReminderRepository reminders;
    private final MemberRepository members;
    private final MessageRepository messages;
    private final JPAQueryFactory jpaQueryFactory;
    private final Clock clock;
    private final SlackIdExtractor slackIdExtractor;

    public ReminderService(final ReminderRepository reminders, final MemberRepository members,
                           final MessageRepository messages, final JPAQueryFactory jpaQueryFactory, final Clock clock,
                           final SlackIdExtractor slackIdExtractor) {
        this.reminders = reminders;
        this.members = members;
        this.messages = messages;
        this.jpaQueryFactory = jpaQueryFactory;
        this.clock = clock;
        this.slackIdExtractor = slackIdExtractor;
    }

    @Transactional
    public void save(final Long memberId, final ReminderSaveRequest request) {
        Member member = members.getById(memberId);

        Message message = messages.getById(request.getMessageId());

        Reminder reminder = new Reminder(member, message, request.getReminderDate());
        reminders.save(reminder);
    }

    public ReminderResponse findOne(final Long messageId, final Long memberId) {
        Reminder reminder = reminders.getByMessageIdAndMemberId(messageId, memberId);

        ReminderResponse response = ReminderResponse.from(reminder);

        replaceMentionMembers(memberId, List.of(response));

        return response;
    }

    public ReminderResponses find(final ReminderFindRequest request, final Long memberId) {
        List<Reminder> reminderList = findReminders(request, memberId);

        List<ReminderResponse> responses = toReminderResponseList(reminderList);
        replaceMentionMembers(memberId, responses);

        return new ReminderResponses(responses, hasPast(reminderList, memberId));
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

        Reminder reminder = reminders.getById(reminderId);

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

    private boolean hasPast(final List<Reminder> reminderList, final Long memberId) {
        if (reminderList.isEmpty()) {
            return false;
        }

        if (isTargetDateMessageLeft(reminderList)) {
            return true;
        }

        Integer result = jpaQueryFactory
                .selectOne()
                .from(QReminder.reminder)
                .where(QReminder.reminder.member.id.eq(memberId))
                .where(meetHasPastCondition(reminderList))
                .fetchFirst();

        return result != null;
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

    private BooleanExpression meetHasPastCondition(final List<Reminder> reminderList) {
        Reminder targetReminder = reminderList.get(reminderList.size() - 1);

        LocalDateTime remindDate = targetReminder.getRemindDate();

        return QReminder.reminder.remindDate.after(remindDate);
    }

    private void replaceMentionMembers(final Long memberId, final List<ReminderResponse> reminderResponses) {
        Member member = members.getById(memberId);
        List<Member> workspaceMembers = members.findAllByWorkspace(member.getWorkspace());

        Map<String, String> workspaceMemberMap = workspaceMembers.stream()
                .collect(Collectors.toMap(Member::getSlackId, Member::getUsername));

        for (ReminderResponse response : reminderResponses) {
            String text = replaceMentionMemberInText(response.getText(), workspaceMemberMap);
            response.replaceText(text);
        }
    }

    private String replaceMentionMemberInText(String text, final Map<String, String> memberMap) {
        Set<String> slackIds = slackIdExtractor.extract(text);
        for (String slackId : slackIds) {
            text = text.replace(
                    MENTION_PREFIX + slackId + MENTION_SUFFIX,
                    memberMap.getOrDefault(slackId, MENTION_PREFIX + slackId + MENTION_SUFFIX));
        }
        return text;
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
