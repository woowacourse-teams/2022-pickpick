package com.pickpick.controller;

import com.pickpick.controller.dto.MessageDto;
import com.pickpick.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private final MessageService messageService;

    public EventController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody Map<String, Object> map) {
        if ("url_verification".equals(String.valueOf(map.get("type")))) {
            return ResponseEntity.ok((String) map.get("challenge"));
        }

        messageService.save(extractMessageDto(map));

        return ResponseEntity.ok("");
    }

    private MessageDto extractMessageDto(Map<String, Object> map) {
        final Map<String, Object> event = (Map<String, Object>) map.get("event");

        return new MessageDto(
                (String) event.get("user"),
                (String) event.get("ts"),
                (String) event.get("ts"),
                (String) event.get("text")
        );
    }
}
