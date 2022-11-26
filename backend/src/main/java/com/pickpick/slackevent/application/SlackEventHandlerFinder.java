package com.pickpick.slackevent.application;

import com.pickpick.exception.slackevent.SlackEventServiceNotFoundException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SlackEventHandlerFinder {

    private final Map<SlackEvent, SlackEventHandler> slackEventServices;

    public SlackEventHandlerFinder(final List<SlackEventHandler> slackEventHandlers) {
        this.slackEventServices = new EnumMap<>(SlackEvent.class);
        for (SlackEventHandler slackEventHandler : slackEventHandlers) {
            this.slackEventServices.put(slackEventHandler.getSlackEvent(), slackEventHandler);
        }
    }

    public SlackEventHandler findBySlackEvent(final SlackEvent slackEvent) {
        SlackEventHandler slackEventHandler = slackEventServices.get(slackEvent);
        if (slackEventHandler == null) {
            throw new SlackEventServiceNotFoundException(slackEvent);
        }
        return slackEventHandler;
    }
}
