package com.pickpick.slackevent.application;

import java.util.Map;

public interface SlackEventService {

    void execute(String requestBody);

    boolean isSameSlackEvent(SlackEvent slackEvent);
}
