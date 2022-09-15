package com.pickpick.slackevent.ui;

import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventServiceFinder;
import com.pickpick.slackevent.ui.dto.ChallengeRequest;
import com.pickpick.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/event")
public class SlackEventController {

    private static final String URL_VERIFICATION = "url_verification";
    private static final String EMPTY_STRING = "";

    private final SlackEventServiceFinder slackEventServiceFinder;

    public SlackEventController(final SlackEventServiceFinder slackEventServiceFinder) {
        this.slackEventServiceFinder = slackEventServiceFinder;
    }

    @PostMapping
    public ResponseEntity<String> save(final @RequestBody String requestBody) {
        log.info("Slack Event: {}", requestBody);

        if (isUrlVerificationRequest(requestBody)) {
            ChallengeRequest challengeRequest = JsonUtils.convert(requestBody, ChallengeRequest.class);
            return ResponseEntity.ok(challengeRequest.getChallenge());
        }

        slackEventServiceFinder.findBySlackEvent(SlackEvent.of(requestBody))
                .execute(requestBody);

        return ResponseEntity.ok(EMPTY_STRING);
    }

    private boolean isUrlVerificationRequest(final String request) {
        ChallengeRequest challengeRequest = JsonUtils.convert(request, ChallengeRequest.class);
        return URL_VERIFICATION.equals(challengeRequest.getType());
    }
}
