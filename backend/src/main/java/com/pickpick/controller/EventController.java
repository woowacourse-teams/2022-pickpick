package com.pickpick.controller;

import com.pickpick.controller.event.SlackEvent;
import com.pickpick.service.SlackEventServiceFinder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private final SlackEventServiceFinder slackEventServiceFinder;

    public EventController(final SlackEventServiceFinder slackEventServiceFinder) {
        this.slackEventServiceFinder = slackEventServiceFinder;
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody Map<String, Object> requestBody) {
        if (isUrlVerificationRequest(requestBody)) {
            return ResponseEntity.ok((String) requestBody.get("challenge"));
        }

        slackEventServiceFinder.findBySlackEvent(SlackEvent.of(requestBody))
                .execute(requestBody);

        return ResponseEntity.ok("");
    }

    private boolean isUrlVerificationRequest(final Map<String, Object> map) {
        return "url_verification".equals(String.valueOf(map.get("type")));
    }
}
