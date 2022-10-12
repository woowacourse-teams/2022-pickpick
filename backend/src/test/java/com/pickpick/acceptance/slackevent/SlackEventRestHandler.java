package com.pickpick.acceptance.slackevent;

import static com.pickpick.acceptance.RestHandler.post;

import com.pickpick.channel.domain.Channel;
import com.pickpick.fixture.ChannelFixture;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.workspace.domain.Workspace;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

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

    public static ExtractableResponse<Response> 채널_생성(final Workspace workspace, final Channel channel) {
        Map<String, Object> request = SlackEventRequestFactory.channelCreatedEvent(workspace.getSlackId(),
                channel.getSlackId(), channel.getName());
        return post(SLACK_EVENT_API_URL, request);
    }

    public static ExtractableResponse<Response> 채널_이름_변경(final Workspace workspace, final Channel channel,
                                                         final String channelNewName) {
        Map<String, Object> request = SlackEventRequestFactory.channelRenameEvent(workspace.getSlackId(),
                channel.getSlackId(), channelNewName);
        return post(SLACK_EVENT_API_URL, request);
    }

    public static void 메시지_목록_생성(final String memberSlackId, final int count) {
        키워드를_포함한_메시지_목록_생성(memberSlackId, count, "");
    }

    public static void 키워드를_포함한_메시지_목록_생성(final String memberSlackId, final int count, final String keyword) {
        long time = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();

        for (int i = 1; i <= count; i++) {
            String timestamp = String.valueOf(time + i);
            Map<String, Object> request = SlackEventRequestFactory.messageCreateEvent(
                    memberSlackId,
                    String.format("MSG_SLACK_ID_%d", i),
                    SlackEvent.MESSAGE_CREATED.getSubtype(),
                    timestamp,
                    String.format("%s %d", keyword, i),
                    ChannelFixture.NOTICE.getSlackId()
            );
            post(SLACK_EVENT_API_URL, request);
        }
    }

    public static ExtractableResponse<Response> 빈_메시지_전송(final String memberSlackId) {
        Map<String, Object> request = SlackEventRequestFactory.emptyMessageCreateEvent(memberSlackId, "MSG12345",
                SlackEvent.MESSAGE_CREATED.getSubtype(), ChannelFixture.NOTICE.getSlackId());
        return post(SLACK_EVENT_API_URL, request);
    }

    public static ExtractableResponse<Response> 메시지_전송(final String memberSlackId) {
        return 메시지_전송(memberSlackId, UUID.randomUUID().toString());
    }

    public static ExtractableResponse<Response> 메시지_전송(final String memberSlackId, final String messageSlackId) {
        return 메시지_전송(memberSlackId, messageSlackId, SlackEvent.MESSAGE_CREATED.getSubtype());
    }

    public static ExtractableResponse<Response> 메시지_전송(final String memberSlackId,
                                                       final ChannelFixture channelFixture) {
        Map<String, Object> request = SlackEventRequestFactory.messageCreateEvent(memberSlackId,
                UUID.randomUUID().toString(), SlackEvent.MESSAGE_CREATED.getSubtype(), channelFixture.getSlackId());
        return post(SLACK_EVENT_API_URL, request);
    }

    public static ExtractableResponse<Response> 메시지_전송(final String memberSlackId, final String messageSlackId,
                                                       final String subtype) {
        Map<String, Object> request = SlackEventRequestFactory.messageCreateEvent(memberSlackId, messageSlackId,
                subtype, ChannelFixture.NOTICE.getSlackId());
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
}
