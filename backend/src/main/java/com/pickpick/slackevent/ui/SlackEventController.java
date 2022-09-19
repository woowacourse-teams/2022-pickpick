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
    public ResponseEntity<String> save(@RequestBody final String requestBody) {
        log.info("Slack Event: {}", requestBody);

        ChallengeRequest challengeRequest = JsonUtils.convert(requestBody, ChallengeRequest.class);
        if (isUrlVerificationRequest(challengeRequest)) {
            return ResponseEntity.ok(challengeRequest.getChallenge());
        }

        slackEventServiceFinder.findBySlackEvent(SlackEvent.of(requestBody))
                .execute(requestBody);

        return ResponseEntity.ok(EMPTY_STRING);
    }

    private boolean isUrlVerificationRequest(final ChallengeRequest challengeRequest) {
        return URL_VERIFICATION.equals(challengeRequest.getType());
    }
}
