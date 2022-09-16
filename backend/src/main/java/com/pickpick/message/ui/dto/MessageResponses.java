package com.pickpick.message.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class MessageResponses {

    private List<MessageResponse> messages;

    private boolean hasPast;

    private boolean hasFuture;

    @JsonProperty(value = "isNeedPastMessage")
    private boolean needPastMessage;

    private MessageResponses() {
    }

    public MessageResponses(final List<MessageResponse> messages, final boolean hasPast, final boolean hasFuture,
                            final boolean needPastMessage) {
        this.messages = messages;
        this.hasPast = hasPast;
        this.hasFuture = hasFuture;
        this.needPastMessage = needPastMessage;
    }
}
