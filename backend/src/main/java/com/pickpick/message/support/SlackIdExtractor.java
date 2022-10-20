package com.pickpick.message.support;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class SlackIdExtractor {

    private static final String SLACK_ID_PATTERN = "<@\\w{11}>";
    private static final Pattern PATTERN = Pattern.compile(SLACK_ID_PATTERN);

    public Set<String> extract(String text) {
        Matcher matcher = PATTERN.matcher(text);

        Set<String> slackIds = new HashSet<>();

        while (matcher.find()) {
            String slackId = extractSlackId(matcher);
            slackIds.add(slackId);
        }

        return slackIds;
    }

    private String extractSlackId(final Matcher matcher) {
        return matcher.group()
                .replace("<@", "")
                .replace(">", "");
    }
}
