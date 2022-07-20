package com.pickpick.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class SlackMessageResponses {

    private List<SlackMessageResponse> messages;

    @JsonProperty(value = "isLast")
    private boolean last;

    private SlackMessageResponses() {
    }

    public SlackMessageResponses(final List<SlackMessageResponse> messages, final boolean last) {
        this.messages = messages;
        this.last = last;
    }
}
