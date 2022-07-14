package com.pickpick.controller;

import com.pickpick.controller.dto.SlackMessageRequest;
import com.pickpick.controller.dto.SlackMessageResponses;
import com.pickpick.exception.WrongMessageRequestException;
import com.pickpick.service.MessageService;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(final MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public SlackMessageResponses findSlackMessages(@Valid SlackMessageRequest slackMessageRequest) {
        if (Objects.nonNull(slackMessageRequest.getDate()) && Objects.nonNull(slackMessageRequest.getMessageId())) {
            throw new WrongMessageRequestException();
        }
        return messageService.find(slackMessageRequest);
    }
}
