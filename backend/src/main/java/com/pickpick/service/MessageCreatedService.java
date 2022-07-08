package com.pickpick.service;

import com.pickpick.controller.dto.MessageDto;
import com.pickpick.controller.event.SlackEvent;
import com.pickpick.entity.User;
import com.pickpick.exception.UserNotFoundException;
import com.pickpick.repository.MessageRepository;
import com.pickpick.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional
@Service
public class MessageCreatedService implements SlackEventService {

    private static final String EVENT = "event";
    private static final String USER = "user";
    private static final String TIMESTAMP = "ts";
    private static final String TEXT = "text";

    private final MessageRepository messages;
    private final UserRepository users;

    public MessageCreatedService(final MessageRepository messages, final UserRepository users) {
        this.messages = messages;
        this.users = users;
    }

    @Override
    public void execute(final Map<String, Object> requestBody) {
        MessageDto messageDto = convert(requestBody);

        User user = users.findBySlackId(messageDto.getUserSlackId())
                .orElseThrow(UserNotFoundException::new);

        messages.save(messageDto.toEntity(user));
    }

    private MessageDto convert(final Map<String, Object> requestBody) {
        final Map<String, Object> event = (Map<String, Object>) requestBody.get(EVENT);

        return new MessageDto(
                (String) event.get(USER),
                (String) event.get(TIMESTAMP),
                (String) event.get(TIMESTAMP),
                (String) event.get(TEXT)
        );
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MESSAGE_CREATED == slackEvent;
    }
}
