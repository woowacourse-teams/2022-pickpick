package com.pickpick.message.application;

import com.pickpick.channel.domain.ChannelSubscription;
import com.pickpick.channel.domain.ChannelSubscriptionRepository;
import com.pickpick.exception.MemberNotFoundException;
import com.pickpick.exception.MessageNotFoundException;
import com.pickpick.exception.SubscriptionNotFoundException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Bookmark;
import com.pickpick.message.domain.BookmarkRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.domain.QMessage;
import com.pickpick.message.ui.dto.MessageRequest;
import com.pickpick.message.ui.dto.MessageResponse;
import com.pickpick.message.ui.dto.MessageResponses;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional(readOnly = true)
@Service
public class MessageService {

    private static final int FIRST_INDEX = 0;
    private static final int ONE_TO_GET_LAST_INDEX = 1;

    private final MessageRepository messageRepository;
    private final ChannelSubscriptionRepository channelSubscriptions;
    private final MemberRepository members;
    private final BookmarkRepository bookmarks;
    private final JPAQueryFactory jpaQueryFactory;

    public MessageService(final MessageRepository messageRepository,
                          final ChannelSubscriptionRepository channelSubscriptions,
                          final MemberRepository members,
                          final BookmarkRepository bookmarks,
                          final JPAQueryFactory jpaQueryFactory) {
        this.messageRepository = messageRepository;
        this.channelSubscriptions = channelSubscriptions;
        this.members = members;
        this.bookmarks = bookmarks;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public MessageResponses find(final Long memberId, final MessageRequest messageRequest) {
        List<Long> channelIds = findChannelId(memberId, messageRequest);

        List<Message> messages = findMessages(channelIds, messageRequest);
        boolean isLast = isLast(channelIds, messageRequest, messages);

        Member member = members.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        return toSlackMessageResponse(messages, isLast, messageRequest.isNeedPastMessage(), member);
    }

    private List<Long> findChannelId(final Long memberId, final MessageRequest messageRequest) {
        if (Objects.nonNull(messageRequest.getChannelIds()) && !messageRequest.getChannelIds().isEmpty()) {
            return messageRequest.getChannelIds();
        }

        ChannelSubscription firstSubscription = channelSubscriptions.findFirstByMemberIdOrderByViewOrderAsc(memberId)
                .orElseThrow(() -> new SubscriptionNotFoundException(memberId));

        return List.of(firstSubscription.getChannelId());
    }

    private List<Message> findMessages(final List<Long> channelIds, final MessageRequest messageRequest) {
        boolean needPastMessage = messageRequest.isNeedPastMessage();
        int messageCount = messageRequest.getMessageCount();

        List<Message> foundMessages = jpaQueryFactory
                .selectFrom(QMessage.message)
                .leftJoin(QMessage.message.member)
                .fetchJoin()
                .where(meetAllConditions(channelIds, messageRequest))
                .orderBy(arrangeDateByNeedPastMessage(needPastMessage))
                .limit(messageCount)
                .fetch();

        if (needPastMessage) {
            return foundMessages;
        }

        return foundMessages.stream()
                .sorted(Comparator.comparing(Message::getPostedDate).reversed())
                .collect(Collectors.toList());
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
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));

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

    private boolean isLast(final List<Long> channelIds, final MessageRequest messageRequest,
                           final List<Message> messages) {
        if (messages.isEmpty()) {
            return true;
        }

        Integer result = jpaQueryFactory
                .selectOne()
                .from(QMessage.message)
                .where(meetAllIsLastCondition(channelIds, messageRequest, messages))
                .fetchFirst();

        return Objects.isNull(result);
    }

    private BooleanExpression meetAllIsLastCondition(final List<Long> channelIds, final MessageRequest request,
                                                     final List<Message> messages) {
        Message targetMessage = findTargetMessage(messages, request.isNeedPastMessage());

        return channelIdsIn(channelIds)
                .and(textContains(request.getKeyword()))
                .and(isBeforeOrAfterTarget(targetMessage.getPostedDate(), request.isNeedPastMessage()));
    }

    private Message findTargetMessage(final List<Message> messages, final boolean needPastMessage) {
        if (needPastMessage) {
            return messages.get(messages.size() - ONE_TO_GET_LAST_INDEX);
        }

        return messages.get(FIRST_INDEX);
    }

    private BooleanExpression isBeforeOrAfterTarget(final LocalDateTime targetPostDate, final boolean needPastMessage) {
        if (needPastMessage) {
            return QMessage.message.postedDate.before(targetPostDate);
        }

        return QMessage.message.postedDate.after(targetPostDate);
    }

    private MessageResponses toSlackMessageResponse(final List<Message> messages, final boolean isLast,
                                                    final boolean needPastMessage, final Member member) {
        return new MessageResponses(toSlackMessageResponses(messages, member), isLast, needPastMessage);
    }

    private List<MessageResponse> toSlackMessageResponses(final List<Message> messages, final Member member) {
        List<MessageResponse> messageResponses = new ArrayList<>();

        for (Message message : messages) {
            Optional<Bookmark> bookmark = bookmarks.findByMessageIdAndMemberId(message.getId(), member.getId());
            boolean isBookmarked = bookmark.isPresent();

            MessageResponse messageResponse = new MessageResponse(
                    message.getId(),
                    member.getId(),
                    member.getUsername(),
                    member.getThumbnailUrl(),
                    message.getText(),
                    message.getPostedDate(),
                    message.getModifiedDate(),
                    isBookmarked
            );
            messageResponses.add(messageResponse);
        }

        return messageResponses;
    }
}
