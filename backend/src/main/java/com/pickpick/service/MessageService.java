package com.pickpick.service;

import com.pickpick.controller.dto.SlackMessageRequest;
import com.pickpick.entity.Message;
import com.pickpick.entity.QMessage;
import com.pickpick.exception.MessageNotFoundException;
import com.pickpick.repository.MessageRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

    public List<Message> find(final SlackMessageRequest slackMessageRequest) {

        jpaQueryFactory = new JPAQueryFactory(entityManager);

        BooleanBuilder builder = createBooleanBuilder(slackMessageRequest);

        List<Long> channelIds = slackMessageRequest.getChannelIds();
        int messageCount = slackMessageRequest.getMessageCount();

        return jpaQueryFactory
                .selectFrom(QMessage.message)
                .leftJoin(QMessage.message.member)
                .fetchJoin()
                .distinct()
                .where(QMessage.message.channel.id.in(channelIds))
                .where(builder)
                .orderBy(QMessage.message.postedDate.desc())
                .limit(messageCount)
                .fetch();
    }

    //TODO https://whitepro.tistory.com/450 BooleanBuilder 리팩터링 참고
    private BooleanBuilder createBooleanBuilder(final SlackMessageRequest slackMessageRequest) {
        BooleanBuilder builder = new BooleanBuilder();

        String keyword = slackMessageRequest.getKeyword();
        if (StringUtils.hasText(keyword)) {
            builder.and(QMessage.message.text.contains(keyword));
        }

        LocalDateTime date = slackMessageRequest.getDate();
        boolean needPastMessage = slackMessageRequest.isNeedPastMessage();
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

        Long messageId = slackMessageRequest.getMessageId();
        if (Objects.nonNull(messageId)) {
            Message message = messageRepository.findById(messageId)
                    .orElseThrow(MessageNotFoundException::new);

            LocalDateTime messageDate = message.getPostedDate();

            if (needPastMessage) {
                builder.and(QMessage.message.postedDate.before(messageDate));
            } else {
                builder.and(QMessage.message.postedDate.after(messageDate));
            }
        }
        return builder;
    }
}
