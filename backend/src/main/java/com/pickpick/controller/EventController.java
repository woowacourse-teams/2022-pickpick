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

    private static final String URL_VERIFICATION = "url_verification";
    private static final String TYPE = "type";
    private static final String CHALLENGE = "challenge";
    private static final String EMPTY_STRING = "";

    private final SlackEventServiceFinder slackEventServiceFinder;

    public EventController(final SlackEventServiceFinder slackEventServiceFinder) {
        this.slackEventServiceFinder = slackEventServiceFinder;
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody Map<String, Object> requestBody) {
        if (isUrlVerificationRequest(requestBody)) {
            return ResponseEntity.ok((String) requestBody.get(CHALLENGE));
        }

        slackEventServiceFinder.findBySlackEvent(SlackEvent.of(requestBody))
                .execute(requestBody);

        return ResponseEntity.ok(EMPTY_STRING);
    }

    private boolean isUrlVerificationRequest(final Map<String, Object> map) {
        return URL_VERIFICATION.equals(String.valueOf(map.get(TYPE)));
    }
}
