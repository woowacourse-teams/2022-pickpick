package com.pickpick.controller;

import com.pickpick.controller.event.SlackEvent;
import com.pickpick.exception.SlackEventNotFoundException;
import com.pickpick.service.SlackEventService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SlackEventServiceFinder {

    public final List<SlackEventService> slackEventServices;

    public SlackEventServiceFinder(final List<SlackEventService> slackEventServices) {
        this.slackEventServices = slackEventServices;
    }

    public SlackEventService find(SlackEvent slackEvent) {
        return slackEventServices.stream()
                .filter(service -> service.isSameSlackEvent(slackEvent))
                .findAny()
                .orElseThrow(SlackEventNotFoundException::new);
    }
}
