package com.pickpick.message.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class MessageResponses implements MessageTextResponses<MessageResponse> {

    private List<MessageResponse> messages;

    @JsonProperty(value = "hasPast")
    private boolean hasPast;

    @JsonProperty(value = "hasFuture")
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

    public List<MessageResponse> getMessages() {
        return messages;
    }

    public boolean hasPast() {
        return this.hasPast;
    }

    public boolean hasFuture() {
        return hasFuture;
    }

    public boolean isNeedPastMessage() {
        return needPastMessage;
    }

    @Override
    public List<MessageResponse> findContents() {
        return messages;
    }
}
