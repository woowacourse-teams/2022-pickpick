package com.pickpick.message.support;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class SlackIdExtractor {

    private static final String SLACK_ID_PATTERN = "<@\\w{11}>";

    public Set<String> extract(String text) {
        Pattern pattern = Pattern.compile(SLACK_ID_PATTERN);
        Matcher matcher = pattern.matcher(text);

        Set<String> slackIds = new HashSet<>();

        while (matcher.find()) {
            String slackId = extractPureSlackId(matcher);
            slackIds.add(slackId);
        }

        return slackIds;
    }

    private String extractPureSlackId(final Matcher matcher) {
        return matcher.group()
                .replace("<@", "")
                .replace(">", "");
    }
}
