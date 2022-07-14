package com.pickpick.controller.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class SlackMessageResponses {

    private final List<SlackMessageResponse> messages;
    private final boolean isLast;

    public SlackMessageResponses(final List<SlackMessageResponse> messages, final boolean isLast) {
        this.messages = messages;
        this.isLast = isLast;
    }
}
