package com.pickpick.slackevent.application.message;

import com.pickpick.exception.message.MessageNotFoundException;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.message.dto.MessageChangedRequest;
import com.pickpick.slackevent.application.message.dto.SlackMessageDto;
import com.pickpick.utils.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MessageChangedService implements SlackEventService {

    private final MessageThreadBroadcastService messageThreadBroadcastService;
    private final MessageRepository messages;

    public MessageChangedService(final MessageThreadBroadcastService messageThreadBroadcastService,
                                 final MessageRepository messages) {
        this.messageThreadBroadcastService = messageThreadBroadcastService;
        this.messages = messages;
    }

    @Override
    public void execute(final String requestBody) {
        MessageChangedRequest request = JsonUtils.convert(requestBody, MessageChangedRequest.class);
        SlackMessageDto slackMessageDto = request.toDto();

        if (isThreadBroadcastEvent(request)) {
            messageThreadBroadcastService.saveWhenSubtypeIsMessageChanged(slackMessageDto);
            return;
        }

        Message message = messages.findBySlackId(slackMessageDto.getSlackId())
                .orElseThrow(() -> new MessageNotFoundException(slackMessageDto.getSlackId()));

        message.changeText(slackMessageDto.getText(), slackMessageDto.getModifiedDate());
    }

    private boolean isThreadBroadcastEvent(final MessageChangedRequest request) {
        String subtype = request.getEvent().getMessage().getSubtype();
        return SlackEvent.MESSAGE_THREAD_BROADCAST.isSameSubtype(subtype);
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MESSAGE_CHANGED == slackEvent;
    }
}
