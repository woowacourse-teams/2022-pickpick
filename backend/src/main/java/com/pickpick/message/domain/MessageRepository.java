package com.pickpick.message.domain;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends Repository<Message, Long> {

    void save(Message message);

    List<Message> findAll();

    Optional<Message> findById(long id);

    Optional<Message> findBySlackId(String slackMessageId);

    void deleteBySlackId(String slackId);

    void deleteAllByChannelSlackId(String channelSlackId);
}
