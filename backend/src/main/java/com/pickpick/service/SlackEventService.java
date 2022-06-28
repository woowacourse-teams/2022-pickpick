package com.pickpick.service;

import com.pickpick.controller.event.SlackEvent;
import java.util.Map;

public interface SlackEventService {

    void execute(Map<String, Object> requestBody);

    boolean isSameSlackEvent(SlackEvent slackEvent);
}
