package com.pickpick.message.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class MessageResponses {

    private List<MessageResponse> messages;

    @JsonProperty(value = "isLast")
    private boolean last;

    private MessageResponses() {
    }

    public MessageResponses(final List<MessageResponse> messages, final boolean last) {
        this.messages = messages;
        this.last = last;
    }
}
