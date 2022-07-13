package com.pickpick.controller.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class SlackMessageResponses {

    private final List<SlackMessageResponse> messages;

    public SlackMessageResponses(final List<SlackMessageResponse> messages) {
        this.messages = messages;
    }
}
