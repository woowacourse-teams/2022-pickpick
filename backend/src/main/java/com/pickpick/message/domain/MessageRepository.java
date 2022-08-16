package com.pickpick.message.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MessageRepository extends Repository<Message, Long> {

    Message save(Message message);

    List<Message> findAll();

    Optional<Message> findById(long id);

    Optional<Message> findBySlackId(String slackMessageId);

    void deleteBySlackId(String slackId);

    void deleteAllByChannelSlackId(String channelSlackId);
}
