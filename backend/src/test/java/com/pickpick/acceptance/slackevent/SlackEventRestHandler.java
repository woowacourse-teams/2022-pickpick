package com.pickpick.acceptance.slackevent;

import static com.pickpick.acceptance.RestHandler.post;

import com.pickpick.channel.domain.Channel;
import com.pickpick.slackevent.application.SlackEvent;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class SlackEventRestHandler {

    private static final String SLACK_EVENT_API_URL = "/api/event";

    private SlackEventRestHandler() {
    }

    public static ExtractableResponse<Response> URL_검증(final String token, final String type, final String challenge) {
        Map<String, Object> request = SlackEventRequestFactory.urlVerifyEvent(token, type, challenge);
        return post(SLACK_EVENT_API_URL, request);
    }

    public static ExtractableResponse<Response> 회원가입(final String slackId, final String workspaceSlackId) {
        Map<String, Object> request = SlackEventRequestFactory.memberJoinEvent(slackId, workspaceSlackId);
        return post(SLACK_EVENT_API_URL, request);
    }

    public static ExtractableResponse<Response> 멤버_정보_수정(final String slackId, final String realName,
                                                         final String displayName, final String thumbnailUrl) {
        Map<String, Object> request = SlackEventRequestFactory.memberUpdateEvent(slackId, realName, displayName,
                thumbnailUrl);
        return post(SLACK_EVENT_API_URL, request);
    }

    public static ExtractableResponse<Response> 메시지_전송(final String memberSlackId, final String messageSlackId,
                                                       final String subtype) {
        Map<String, Object> request = SlackEventRequestFactory.messageCreateEvent(memberSlackId, messageSlackId,
                subtype);
        return post(SLACK_EVENT_API_URL, request);
    }

    public static ExtractableResponse<Response> 메시지_수정(final String memberSlackId, final String messageSlackId) {
        return 메시지_전송(memberSlackId, messageSlackId, SlackEvent.MESSAGE_CHANGED.getSubtype());
    }

    public static ExtractableResponse<Response> 메시지_삭제(final String memberSlackId, final String messageSlackId) {
        return 메시지_전송(memberSlackId, messageSlackId, SlackEvent.MESSAGE_DELETED.getSubtype());
    }

    public static ExtractableResponse<Response> 브로드캐스트_메시지_전송(final String memberSlackId) {
        Map<String, Object> request = SlackEventRequestFactory.threadBroadcastCreateEvent(memberSlackId);
        return post(SLACK_EVENT_API_URL, request);
    }

    public static void 채널_생성(final String memberSlackId, final Channel channel) {
        post(SLACK_EVENT_API_URL, SlackEventRequestFactory.channelCreateEvent(memberSlackId, channel));
    }
}
