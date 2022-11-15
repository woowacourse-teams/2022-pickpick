package com.pickpick.slackevent.application;

import com.pickpick.exception.slackevent.SlackEventServiceNotFoundException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SlackEventServiceFinder {

    private final Map<SlackEvent, SlackEventService> slackEventServices;

    public SlackEventServiceFinder(final List<SlackEventService> slackEventServices) {
        this.slackEventServices = new EnumMap<>(SlackEvent.class);
        for (SlackEventService slackEventService : slackEventServices) {
            this.slackEventServices.put(slackEventService.getSlackEvent(), slackEventService);
        }
    }

    public SlackEventService findBySlackEvent(final SlackEvent slackEvent) {
        SlackEventService slackEventService = slackEventServices.get(slackEvent);
        if (slackEventService == null) {
            throw new SlackEventServiceNotFoundException(slackEvent);
        }
        return slackEventService;
    }
}
