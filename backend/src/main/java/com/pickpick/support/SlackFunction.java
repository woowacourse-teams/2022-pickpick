package com.pickpick.support;

import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.SlackApiRequest;
import com.slack.api.methods.SlackApiTextResponse;
import java.io.IOException;

@FunctionalInterface
public interface SlackFunction<T extends SlackApiTextResponse, K extends SlackApiRequest> {

    T execute(K request) throws IOException, SlackApiException;
}
