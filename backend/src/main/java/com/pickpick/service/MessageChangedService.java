package com.pickpick.service;

import com.pickpick.controller.dto.MessageDto;
import com.pickpick.controller.event.SlackEvent;
import com.pickpick.entity.Message;
import com.pickpick.exception.MessageNotFoundException;
import com.pickpick.repository.MessageRepository;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MessageChangedService implements SlackEventService {

    private static final String EVENT = "event";
    private static final String USER = "user";
    private static final String TIMESTAMP = "ts";
    private static final String TEXT = "text";
    private static final String CLIENT_MSG_ID = "client_msg_id";
    private static final String CHANNEL = "channel";
    private static final String MESSAGE = "message";

    private final MessageRepository messages;

    public MessageChangedService(final MessageRepository messages) {
        this.messages = messages;
    }

    @Override
    public void execute(final Map<String, Object> requestBody) {
        MessageDto messageDto = convert(requestBody);

        Message message = messages.findBySlackId(messageDto.getSlackId())
                .orElseThrow(() -> new MessageNotFoundException(messageDto.getSlackId()));

        message.changeText(messageDto.getText(), messageDto.getModifiedDate());
    }

    private MessageDto convert(final Map<String, Object> requestBody) {
        Map<String, Object> event = (Map) requestBody.get(EVENT);
        Map<String, Object> message = (Map) event.get(MESSAGE);

        return new MessageDto(
                (String) message.get(USER),
                (String) message.get(CLIENT_MSG_ID),
                (String) message.get(TIMESTAMP),
                (String) message.get(TIMESTAMP),
                (String) message.get(TEXT),
                (String) event.get(CHANNEL)
        );
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MESSAGE_CHANGED == slackEvent;
    }
}
