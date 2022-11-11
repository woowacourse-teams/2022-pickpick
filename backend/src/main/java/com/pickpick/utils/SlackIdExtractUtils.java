package com.pickpick.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlackIdExtractUtils {

    private static final String SLACK_ID_PATTERN = "<@\\w{11}>";
    private static final Pattern PATTERN = Pattern.compile(SLACK_ID_PATTERN);

    public static Set<String> extract(final String text) {
        Matcher matcher = PATTERN.matcher(text);

        Set<String> slackIds = new HashSet<>();

        while (matcher.find()) {
            String slackId = extractSlackId(matcher);
            slackIds.add(slackId);
        }

        return slackIds;
    }

    private static String extractSlackId(final Matcher matcher) {
        return matcher.group()
                .replace("<@", "")
                .replace(">", "");
    }
}
