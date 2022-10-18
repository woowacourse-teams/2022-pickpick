package com.pickpick.message.domain;

import com.pickpick.exception.message.MessageNotFoundException;
import com.pickpick.message.ui.dto.MessageRequest;
import com.pickpick.message.ui.dto.MessageResponse;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class QMessageRepository {

    private final QMessage message = QMessage.message;
    private final QBookmark bookmark = QBookmark.bookmark;
    private final QReminder reminder = QReminder.reminder;

    private final JPAQueryFactory jpaQueryFactory;
    private final Clock clock;

    public QMessageRepository(final JPAQueryFactory jpaQueryFactory, final Clock clock) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.clock = clock;
    }

    public List<MessageResponse> findMessages(final Long memberId, final List<Long> channelIds,
                                              final MessageRequest messageRequest) {
        boolean needPastMessage = messageRequest.isNeedPastMessage();
        int messageCount = messageRequest.getMessageCount();

        return jpaQueryFactory
                .select(getMessageResponseConstructor())
                .from(message)
                .leftJoin(message.member)
                .leftJoin(bookmark)
                .on(existsBookmark(memberId))
                .leftJoin(reminder)
                .on(remainReminder(memberId))
                .where(meetAllConditions(channelIds, messageRequest))
                .orderBy(arrangeDateByNeedPastMessage(needPastMessage))
                .limit(messageCount)
                .fetch();
    }

    public boolean hasPast(final List<Long> channelIds, final MessageRequest messageRequest,
                           final List<MessageResponse> messages) {
        Integer result = jpaQueryFactory
                .selectOne()
                .from(message)
                .where(meetAllHasPastCondition(channelIds, messageRequest, messages))
                .fetchFirst();

        return result != null;
    }

    public boolean hasFuture(final List<Long> channelIds, final MessageRequest messageRequest,
                             final List<MessageResponse> messages) {
        Integer result = jpaQueryFactory
                .selectOne()
                .from(message)
                .where(meetAllHasFutureCondition(channelIds, messageRequest, messages))
                .fetchFirst();

        return result != null;
    }

    private ConstructorExpression<MessageResponse> getMessageResponseConstructor() {
        return Projections.constructor(MessageResponse.class,
                message.id,
                message.member.id,
                message.member.username,
                message.member.thumbnailUrl,
                message.text,
                message.postedDate,
                message.modifiedDate,
                bookmark.id,
                reminder.id,
                reminder.remindDate);
    }

    private BooleanExpression existsBookmark(final Long memberId) {
        return bookmark.member.id.eq(memberId)
                .and(bookmark.message.id.eq(message.id));
    }

    private BooleanExpression remainReminder(final Long memberId) {
        return reminder.member.id.eq(memberId)
                .and(reminder.message.id.eq(message.id))
                .and(reminder.remindDate.after(LocalDateTime.now(clock)));
    }

    private BooleanExpression meetAllConditions(final List<Long> channelIds, final MessageRequest request) {
        return channelIdsIn(channelIds)
                .and(textContains(request.getKeyword()))
                .and(messageHasText())
                .and(decideMessageIdOrDate(request.getMessageId(), request.getDate(), request.isNeedPastMessage()));
    }

    private BooleanExpression channelIdsIn(final List<Long> channelIds) {
        return message.channel.id.in(channelIds);
    }

    private BooleanExpression textContains(final String keyword) {
        if (StringUtils.hasText(keyword)) {
            return message.text.containsIgnoreCase(keyword);
        }

        return null;
    }

    private Predicate decideMessageIdOrDate(final Long messageId,
                                            final LocalDateTime date,
                                            final boolean needPastMessage) {
        if (messageId != null) {
            return messageIdCondition(messageId, needPastMessage);
        }

        return dateCondition(date, needPastMessage);
    }

    private BooleanExpression messageHasText() {
        return message.text.isNotNull()
                .and(message.text.isNotEmpty());
    }


    private Predicate messageIdCondition(final Long messageId, final boolean needPastMessage) {
        Message target = Optional.ofNullable(jpaQueryFactory
                        .select(message)
                        .from(message)
                        .where(message.id.eq(messageId))
                        .fetchOne())
                .orElseThrow(() -> new MessageNotFoundException(messageId));

        LocalDateTime messageDate = target.getPostedDate();

        if (needPastMessage) {
            return message.postedDate.before(messageDate);
        }

        return message.postedDate.after(messageDate);
    }

    private Predicate dateCondition(final LocalDateTime date, final boolean needPastMessage) {
        if (Objects.isNull(date)) {
            return null;
        }

        if (needPastMessage) {
            return message.postedDate.eq(date)
                    .or(message.postedDate.before(date));
        }

        return message.postedDate.eq(date)
                .or(message.postedDate.after(date));
    }

    private OrderSpecifier<LocalDateTime> arrangeDateByNeedPastMessage(final boolean needPastMessage) {
        if (needPastMessage) {
            return message.postedDate.desc();
        }

        return message.postedDate.asc();
    }

    private BooleanExpression meetAllHasPastCondition(final List<Long> channelIds, final MessageRequest request,
                                                      final List<MessageResponse> messages) {
        MessageResponse targetMessage = messages.get(messages.size() - 1);

        return channelIdsIn(channelIds)
                .and(textContains(request.getKeyword()))
                .and(message.postedDate.before(targetMessage.getPostedDate()));
    }

    private BooleanExpression meetAllHasFutureCondition(final List<Long> channelIds, final MessageRequest request,
                                                        final List<MessageResponse> messages) {
        MessageResponse targetMessage = messages.get(0);

        return channelIdsIn(channelIds)
                .and(textContains(request.getKeyword()))
                .and(message.postedDate.after(targetMessage.getPostedDate()));
    }
}
