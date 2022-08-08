package com.pickpick.slackevent.application;

import com.pickpick.exception.slackevent.SlackEventServiceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Component;

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
                .orElseThrow(() -> new SlackEventServiceNotFoundException(slackEvent));
    }
}
