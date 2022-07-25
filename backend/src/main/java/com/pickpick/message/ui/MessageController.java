package com.pickpick.message.ui;

import com.pickpick.auth.support.AuthenticationPrincipal;
import com.pickpick.message.application.MessageService;
import com.pickpick.message.ui.dto.MessageRequest;
import com.pickpick.message.ui.dto.MessageResponses;
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
    public MessageResponses findSlackMessages(final @AuthenticationPrincipal Long memberId,
                                              final @Valid MessageRequest messageRequest) {
        return messageService.find(memberId, messageRequest);
    }
}
