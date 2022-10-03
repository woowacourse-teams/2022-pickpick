package com.pickpick.message.application;

import com.pickpick.channel.domain.ChannelSubscription;
import com.pickpick.channel.domain.ChannelSubscriptionRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.domain.QBookmark;
import com.pickpick.message.domain.QMessage;
import com.pickpick.message.domain.QReminder;
import com.pickpick.message.ui.dto.MessageRequest;
import com.pickpick.message.ui.dto.MessageResponse;
import com.pickpick.message.ui.dto.MessageResponses;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional(readOnly = true)
@Service
public class MessageService {

    private final MessageRepository messages;
    private final ChannelSubscriptionRepository channelSubscriptions;
    private final JPAQueryFactory jpaQueryFactory;
    private final Clock clock;

    public MessageService(final MessageRepository messages,
                          final ChannelSubscriptionRepository channelSubscriptions,
                          final JPAQueryFactory jpaQueryFactory,
                          final Clock clock) {
        this.messages = messages;
        this.channelSubscriptions = channelSubscriptions;
        this.jpaQueryFactory = jpaQueryFactory;
        this.clock = clock;
    }

    public MessageResponses find(final Long memberId, final MessageRequest messageRequest) {
        List<Long> channelIds = findChannelId(memberId, messageRequest);

        List<MessageResponse> messageResponses = findMessages(memberId, channelIds, messageRequest);
        boolean hasPast = hasPast(channelIds, messageRequest, messageResponses);
        boolean hasFuture = hasFuture(channelIds, messageRequest, messageResponses);

        return new MessageResponses(messageResponses, hasPast, hasFuture, messageRequest.isNeedPastMessage());
    }

    private List<Long> findChannelId(final Long memberId, final MessageRequest messageRequest) {
        List<Long> channelIds = messageRequest.getChannelIds();

        if (isNonNullNorEmpty(channelIds)) {
            return channelIds;
        }

        ChannelSubscription firstSubscription = channelSubscriptions.getFirstByMemberIdOrderByViewOrderAsc(memberId);

        return List.of(firstSubscription.getChannelId());
    }

    private static boolean isNonNullNorEmpty(final List<Long> channelIds) {
        return Objects.nonNull(channelIds) && !channelIds.isEmpty();
    }

    private List<MessageResponse> findMessages(final Long memberId, final List<Long> channelIds,
                                               final MessageRequest messageRequest) {
        boolean needPastMessage = messageRequest.isNeedPastMessage();
        int messageCount = messageRequest.getMessageCount();

        List<MessageResponse> messageResponses = jpaQueryFactory
                .select(getMessageResponseConstructor())
                .from(QMessage.message)
                .leftJoin(QMessage.message.member)
                .leftJoin(QBookmark.bookmark)
                .on(existsBookmark(memberId))
                .leftJoin(QReminder.reminder)
                .on(remainReminder(memberId))
                .where(meetAllConditions(channelIds, messageRequest))
                .orderBy(arrangeDateByNeedPastMessage(needPastMessage))
                .limit(messageCount)
                .fetch();

        if (needPastMessage) {
            return messageResponses;
        }

        return messageResponses.stream()
                .sorted(Comparator.comparing(MessageResponse::getPostedDate).reversed())
                .collect(Collectors.toList());
    }

    private ConstructorExpression<MessageResponse> getMessageResponseConstructor() {
        return Projections.constructor(MessageResponse.class,
                QMessage.message.id,
                QMessage.message.member.id,
                QMessage.message.member.username,
                QMessage.message.member.thumbnailUrl,
                QMessage.message.text,
                QMessage.message.postedDate,
                QMessage.message.modifiedDate,
                QBookmark.bookmark.id,
                QReminder.reminder.id,
                QReminder.reminder.remindDate);
    }

    private BooleanExpression existsBookmark(final Long memberId) {
        return QBookmark.bookmark.member.id.eq(memberId)
                .and(QBookmark.bookmark.message.id.eq(QMessage.message.id));
    }

    private BooleanExpression remainReminder(final Long memberId) {
        return QReminder.reminder.member.id.eq(memberId)
                .and(QReminder.reminder.message.id.eq(QMessage.message.id))
                .and(QReminder.reminder.remindDate.after(LocalDateTime.now(clock)));
    }

    private BooleanExpression meetAllConditions(final List<Long> channelIds, final MessageRequest request) {
        return channelIdsIn(channelIds)
                .and(textContains(request.getKeyword()))
                .and(messageHasText())
                .and(decideMessageIdOrDate(request.getMessageId(), request.getDate(), request.isNeedPastMessage()));
    }

    private BooleanExpression channelIdsIn(final List<Long> channelIds) {
        return QMessage.message.channel.id.in(channelIds);
    }

    private BooleanExpression textContains(final String keyword) {
        if (StringUtils.hasText(keyword)) {
            return QMessage.message.text.containsIgnoreCase(keyword);
        }

        return null;
    }

    private Predicate decideMessageIdOrDate(final Long messageId,
                                            final LocalDateTime date,
                                            final boolean needPastMessage) {
        if (Objects.nonNull(messageId)) {
            return messageIdCondition(messageId, needPastMessage);
        }

        return dateCondition(date, needPastMessage);
    }

    private BooleanExpression messageHasText() {
        return QMessage.message.text.isNotNull()
                .and(QMessage.message.text.isNotEmpty());
    }


    private Predicate messageIdCondition(final Long messageId, final boolean needPastMessage) {
        Message message = messages.getById(messageId);

        LocalDateTime messageDate = message.getPostedDate();

        if (needPastMessage) {
            return QMessage.message.postedDate.before(messageDate);
        }

        return QMessage.message.postedDate.after(messageDate);
    }

    private Predicate dateCondition(final LocalDateTime date, final boolean needPastMessage) {
        if (Objects.isNull(date)) {
            return null;
        }

        if (needPastMessage) {
            return QMessage.message.postedDate.eq(date)
                    .or(QMessage.message.postedDate.before(date));
        }

        return QMessage.message.postedDate.eq(date)
                .or(QMessage.message.postedDate.after(date));
    }

    private OrderSpecifier<LocalDateTime> arrangeDateByNeedPastMessage(final boolean needPastMessage) {
        if (needPastMessage) {
            return QMessage.message.postedDate.desc();
        }

        return QMessage.message.postedDate.asc();
    }

    private boolean hasPast(final List<Long> channelIds, final MessageRequest messageRequest,
                            final List<MessageResponse> messages) {
        if (messages.isEmpty()) {
            return false;
        }

        Integer result = jpaQueryFactory
                .selectOne()
                .from(QMessage.message)
                .where(meetAllHasPastCondition(channelIds, messageRequest, messages))
                .fetchFirst();

        return result != null;
    }

    private boolean hasFuture(final List<Long> channelIds, final MessageRequest messageRequest,
                              final List<MessageResponse> messages) {
        if (messages.isEmpty()) {
            return false;
        }

        Integer result = jpaQueryFactory
                .selectOne()
                .from(QMessage.message)
                .where(meetAllHasFutureCondition(channelIds, messageRequest, messages))
                .fetchFirst();

        return result != null;
    }

    private BooleanExpression meetAllHasPastCondition(final List<Long> channelIds, final MessageRequest request,
                                                      final List<MessageResponse> messages) {
        MessageResponse targetMessage = messages.get(messages.size() - 1);

        return channelIdsIn(channelIds)
                .and(textContains(request.getKeyword()))
                .and(QMessage.message.postedDate.before(targetMessage.getPostedDate()));
    }

    private BooleanExpression meetAllHasFutureCondition(final List<Long> channelIds, final MessageRequest request,
                                                        final List<MessageResponse> messages) {
        MessageResponse targetMessage = messages.get(0);

        return channelIdsIn(channelIds)
                .and(textContains(request.getKeyword()))
                .and(QMessage.message.postedDate.after(targetMessage.getPostedDate()));
    }
}
