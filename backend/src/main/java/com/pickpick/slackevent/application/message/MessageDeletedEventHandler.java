package com.pickpick.slackevent.application.message;

import com.pickpick.message.domain.MessageRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventHandler;
import com.pickpick.slackevent.application.message.dto.MessageDeletedRequest;
import com.pickpick.utils.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MessageDeletedEventHandler implements SlackEventHandler {

    private final MessageRepository messages;

    public MessageDeletedEventHandler(final MessageRepository messages) {
        this.messages = messages;
    }

    @Override
    public void execute(final String requestBody) {
        String slackId = extractMessageSlackId(requestBody);

        messages.deleteBySlackId(slackId);
    }

    private String extractMessageSlackId(final String requestBody) {
        MessageDeletedRequest request = JsonUtils.convert(requestBody, MessageDeletedRequest.class);
        return request.getEvent().getPreviousMessage().getClientMsgId();
    }

    @Override
    public SlackEvent getSlackEvent() {
        return SlackEvent.MESSAGE_DELETED;
    }
}
