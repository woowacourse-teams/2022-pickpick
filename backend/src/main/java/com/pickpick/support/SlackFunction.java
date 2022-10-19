package com.pickpick.support;

import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.SlackApiTextResponse;
import java.io.IOException;

@FunctionalInterface
public interface SlackFunction<T extends SlackApiTextResponse> {

    T execute() throws IOException, SlackApiException;
}
