package com.pickpick.controller;

import com.pickpick.controller.dto.SlackMessageRequest;
import com.pickpick.controller.dto.SlackMessageResponses;
import com.pickpick.service.MessageService;
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
        return messageService.find(slackMessageRequest);
    }
}
