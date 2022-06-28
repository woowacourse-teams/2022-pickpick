package com.pickpick.repository;

import com.pickpick.entity.Message;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface MessageRepository extends Repository<Message, Long> {
    void save(Message message);
    List<Message> findAll();
}
