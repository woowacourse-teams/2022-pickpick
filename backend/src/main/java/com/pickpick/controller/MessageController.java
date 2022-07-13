package com.pickpick.controller;

import com.pickpick.controller.dto.SlackMessageResponses;
import com.pickpick.exception.WrongMessageRequestException;
import com.pickpick.service.MessageService;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(final MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public SlackMessageResponses findSlackMessages(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam Long channelId,
            @RequestParam(required = false, defaultValue = "true") boolean needPastMessage,
            @RequestParam(required = false) Long messageId,
            @RequestParam(defaultValue = "20") int messageCount) {
        if (Objects.nonNull(date) && Objects.nonNull(messageId)) {
            throw new WrongMessageRequestException("date와 messageId를 동시에 보내는 것은 불가능합니다.");
        }
        return messageService.find(keyword, date, channelId, needPastMessage, messageId, messageCount);
    }

}
