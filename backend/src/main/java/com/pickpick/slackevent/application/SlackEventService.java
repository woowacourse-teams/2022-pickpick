package com.pickpick.slackevent.application;

import java.util.Map;

public interface SlackEventService {

    void execute(Map<String, Object> requestBody);

    boolean isSameSlackEvent(SlackEvent slackEvent);
}
