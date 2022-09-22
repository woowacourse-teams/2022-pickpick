package com.pickpick.slackevent.ui.dto;

import lombok.Getter;

@Getter
public class ChallengeRequest {

    private String token;
    private String type;
    private String challenge;

    private ChallengeRequest() {
    }

    public ChallengeRequest(final String token, final String type, final String challenge) {
        this.token = token;
        this.type = type;
        this.challenge = challenge;
    }
}
