package com.pickpick.service;

import com.pickpick.controller.dto.SlackMessageRequest;
import com.pickpick.controller.dto.SlackMessageResponse;
import com.pickpick.controller.dto.SlackMessageResponses;
import com.pickpick.entity.Message;
import com.pickpick.entity.QMessage;
import com.pickpick.exception.MessageNotFoundException;
import com.pickpick.repository.MessageRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional(readOnly = true)
@Service
public class MessageService {

    private final EntityManager entityManager;
    private final MessageRepository messageRepository;
    private JPAQueryFactory jpaQueryFactory;

    public MessageService(final EntityManager entityManager, final MessageRepository messageRepository) {
        this.entityManager = entityManager;
        this.messageRepository = messageRepository;
    }

    public SlackMessageResponses find(final SlackMessageRequest slackMessageRequest) {
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        List<Message> messages = findMessages(slackMessageRequest);
        boolean isLast = isLast(slackMessageRequest, messages);

        return toSlackMessageResponse(messages, isLast);
    }

    private List<Message> findMessages(final SlackMessageRequest slackMessageRequest) {
        boolean needPastMessage = slackMessageRequest.isNeedPastMessage();
        BooleanBuilder builder = createFindMessagesCondition(slackMessageRequest);

        List<Message> foundMessages = jpaQueryFactory
                .selectFrom(QMessage.message)
                .leftJoin(QMessage.message.member)
                .fetchJoin()
                .where(QMessage.message.channel.id.in(slackMessageRequest.getChannelIds()))
                .where(builder)
                .orderBy(getTimeCondition(needPastMessage))
                .limit(slackMessageRequest.getMessageCount())
                .fetch();

        if (needPastMessage) {
            return foundMessages;
        }

        return foundMessages.stream()
                .sorted(Comparator.comparing(Message::getPostedDate).reversed())
                .collect(Collectors.toList());
    }

    private OrderSpecifier<LocalDateTime> getTimeCondition(final boolean needPastMessage) {
        if (needPastMessage) {
            return QMessage.message.postedDate.desc();
        }

        return QMessage.message.postedDate.asc();
    }

    private BooleanBuilder createFindMessagesCondition(final SlackMessageRequest slackMessageRequest) {
        BooleanBuilder builder = new BooleanBuilder();

        String keyword = slackMessageRequest.getKeyword();
        if (StringUtils.hasText(keyword)) {
            builder.and(QMessage.message.text.contains(keyword));
        }

        Long messageId = slackMessageRequest.getMessageId();
        boolean needPastMessage = slackMessageRequest.isNeedPastMessage();

        if (Objects.nonNull(messageId)) {
            Message message = messageRepository.findById(messageId)
                    .orElseThrow(MessageNotFoundException::new);

            LocalDateTime messageDate = message.getPostedDate();

            if (needPastMessage) {
                builder.and(QMessage.message.postedDate.before(messageDate));
            } else {
                builder.and(QMessage.message.postedDate.after(messageDate));
            }

            return builder;
        }

        LocalDateTime date = slackMessageRequest.getDate();
        if (Objects.nonNull(date)) {
            if (needPastMessage) {
                builder.and(
                        QMessage.message.postedDate.eq(date)
                                .or(QMessage.message.postedDate.before(date))
                );
                builder.and(QMessage.message.postedDate.before(date));
            } else {
                builder.and(
                        QMessage.message.postedDate.eq(date)
                                .or(QMessage.message.postedDate.after(date))
                );
            }
        }

        return builder;
    }

    private boolean isLast(final SlackMessageRequest slackMessageRequest, final List<Message> messages) {
        if (messages.isEmpty()) {
            return true;
        }

        BooleanBuilder builder = createIsLastCondition(slackMessageRequest);
        Message targetMessage = getTargetMessage(messages, slackMessageRequest.isNeedPastMessage());

        Integer result = jpaQueryFactory
                .selectOne()
                .from(QMessage.message)
                .where(QMessage.message.channel.id.in(slackMessageRequest.getChannelIds()))
                .where(isLastExpression(targetMessage, slackMessageRequest.isNeedPastMessage()))
                .where(builder)
                .fetchFirst();

        return Objects.isNull(result);
    }

    private BooleanExpression isLastExpression(final Message oldestMessage, final boolean needPastMessage) {
        if (needPastMessage) {
            return QMessage.message.postedDate.before(oldestMessage.getPostedDate());
        }

        return QMessage.message.postedDate.after(oldestMessage.getPostedDate());
    }

    private Message getTargetMessage(final List<Message> messages, final boolean needPastMessage) {
        if (needPastMessage) {
            return messages.get(0);
        }

        return messages.get(messages.size() - 1);
    }

    private BooleanBuilder createIsLastCondition(final SlackMessageRequest slackMessageRequest) {
        BooleanBuilder builder = new BooleanBuilder();

        String keyword = slackMessageRequest.getKeyword();
        if (StringUtils.hasText(keyword)) {
            builder.and(QMessage.message.text.contains(keyword));
        }

        return builder;
    }

    private SlackMessageResponses toSlackMessageResponse(final List<Message> messages, final boolean isLast) {
        return new SlackMessageResponses(toSlackMessageResponseList(messages), isLast);
    }

    private List<SlackMessageResponse> toSlackMessageResponseList(final List<Message> messages) {
        return messages.stream()
                .map(SlackMessageResponse::from)
                .collect(Collectors.toList());
    }
}
