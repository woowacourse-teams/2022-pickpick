package com.pickpick.service;

import com.pickpick.controller.dto.MessageDto;
import com.pickpick.entity.User;
import com.pickpick.exception.UserNotFoundException;
import com.pickpick.repository.MessageRepository;
import com.pickpick.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MessageService {

    private final MessageRepository messages;
    private final UserRepository users;

    public MessageService(
            final MessageRepository messages,
            final UserRepository users) {
        this.messages = messages;
        this.users = users;
    }

    @Transactional
    public void save(MessageDto messageDto) {
        User user = users.findBySlackId(messageDto.getUserSlackId())
                .orElseThrow(UserNotFoundException::new);
        messages.save(messageDto.toEntity(user));
    }
}
