package com.pickpick.repository;

import com.pickpick.entity.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MessageRepository extends Repository<Message, Long> {

    void save(Message message);

    List<Message> findAll();

    Optional<Message> findBySlackId(String slackMessageId);
}
