package com.pickpick.acceptance.slackevent;

import static com.pickpick.acceptance.RestHandler.post;

import com.pickpick.channel.domain.Channel;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class SlackEventRestHandler {

    private static final String SLACK_EVENT_API_URL = "/api/event";

    private SlackEventRestHandler() {
    }

    public static ExtractableResponse<Response> 회원가입(final String slackId) {
        Map<String, Object> request = SlackEventRequestFactory.memberJoinEvent(slackId);
        return post(SLACK_EVENT_API_URL, request);
    }
    
    public static ExtractableResponse<Response> 멤버_정보_수정(final String slackId, final String realName,
                                                         final String displayName, final String thumbnailUrl) {
        Map<String, Object> request = SlackEventRequestFactory.memberUpdateEvent(slackId, realName, displayName,
                thumbnailUrl);
        return post(SLACK_EVENT_API_URL, request);
    }

    public static void 채널_생성(final String memberSlackId, final Channel channel) {
        post(SLACK_EVENT_API_URL, SlackEventRequestFactory.channelCreateEvent(memberSlackId, channel));
    }
}
