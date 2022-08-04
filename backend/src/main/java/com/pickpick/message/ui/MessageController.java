package com.pickpick.message.ui;

import com.pickpick.message.application.MessageService;
import com.pickpick.message.ui.dto.MessageRequest;
import com.pickpick.message.ui.dto.MessageResponses;
import com.pickpick.utils.AuthorizationExtractor;
import javax.servlet.http.HttpServletRequest;
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
    public MessageResponses findSlackMessages(HttpServletRequest httpServletRequest,
                                              @Valid MessageRequest messageRequest) {

        String memberId = AuthorizationExtractor.extract(httpServletRequest);

        return messageService.find(Long.parseLong(memberId), messageRequest);
    }
}
