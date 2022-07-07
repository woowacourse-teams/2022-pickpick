package com.pickpick.service;

import com.pickpick.controller.dto.MessageDto;
import com.pickpick.controller.event.SlackEvent;
import com.pickpick.entity.User;
import com.pickpick.exception.UserNotFoundException;
import com.pickpick.repository.MessageRepository;
import com.pickpick.repository.UserRepository;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MessageSaveService implements SlackEventService {

    private final MessageRepository messages;
    private final UserRepository users;

    public MessageSaveService(final MessageRepository messages, final UserRepository users) {
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

    private MessageDto convert(final Map<String, Object> eventBody) {
        final Map<String, Object> event = (Map<String, Object>) eventBody.get("event");

        return new MessageDto(
                (String) event.get("user"),
                (String) event.get("ts"),
                (String) event.get("ts"),
                (String) event.get("text")
        );
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MESSAGE_CREATED == slackEvent;
    }
}
