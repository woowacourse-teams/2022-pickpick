package com.pickpick.slackevent.application;

public interface SlackEventService {

    void execute(String requestBody);

    SlackEvent getSlackEvent();
}
