package com.pickpick.service;

import com.pickpick.controller.event.SlackEvent;
import com.pickpick.exception.SlackEventNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SlackEventServiceFinder {

    public final List<SlackEventService> slackEventServices;

    public SlackEventServiceFinder(final List<SlackEventService> slackEventServices) {
        this.slackEventServices = slackEventServices;
    }

    public SlackEventService findBySlackEvent(final SlackEvent slackEvent) {
        return slackEventServices.stream()
                .filter(service -> service.isSameSlackEvent(slackEvent))
                .findAny()
                .orElseThrow(SlackEventNotFoundException::new);
    }
}
