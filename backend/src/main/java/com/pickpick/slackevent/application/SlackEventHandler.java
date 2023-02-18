package com.pickpick.slackevent.application;

public interface SlackEventHandler {

    void execute(String requestBody);

    SlackEvent getSlackEvent();
}
