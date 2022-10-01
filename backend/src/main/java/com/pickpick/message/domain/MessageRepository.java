package com.pickpick.message.domain;

import com.pickpick.exception.message.MessageNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MessageRepository extends Repository<Message, Long> {

    Message save(Message message);

    List<Message> findAll();

    Optional<Message> findById(long id);

    Optional<Message> findBySlackId(String slackId);

    void deleteBySlackId(String slackId);

    void deleteAllByChannelSlackId(String channelSlackId);

    default Message getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
    }

    default Message getBySlackId(final String slackId) {
        return findBySlackId(slackId)
                .orElseThrow(() -> new MessageNotFoundException(slackId));
    }
}
