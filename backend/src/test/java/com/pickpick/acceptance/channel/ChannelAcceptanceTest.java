package com.pickpick.acceptance.channel;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.channel.ChannelRestHandler.유저_전체_채널_목록_조회_요청;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.채널_생성;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.회원가입;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.fixture.ChannelFixtures;
import com.slack.api.RequestConfigurator;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsInfoRequest.ConversationsInfoRequestBuilder;
import com.slack.api.methods.response.conversations.ConversationsInfoResponse;
import com.slack.api.model.Conversation;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("채널 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
public class ChannelAcceptanceTest extends AcceptanceTest {

    @Test
    void 유저_전체_채널_목록_조회() throws SlackApiException, IOException {
        // given
        String memberSlackId = "userSlackId";
        회원가입(memberSlackId);
        채널_목록_생성(memberSlackId);
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 유저_전체_채널_목록_조회_요청(token);

        // then
        List<ChannelResponse> channels = response.jsonPath().getList("channels.", ChannelResponse.class);

        상태코드_200_확인(response);
        assertThat(channels).hasSize(6);
    }

    private void 채널_목록_생성(final String memberSlackId) throws SlackApiException, IOException {
        for (Channel channel : ChannelFixtures.allChannels()) {
            given(slackClient.conversationsInfo((RequestConfigurator<ConversationsInfoRequestBuilder>) any()))
                    .willReturn(setUpChannelMockData(channel));
            채널_생성(memberSlackId, channel);
        }
    }

    private ConversationsInfoResponse setUpChannelMockData(final Channel channel) {
        Conversation conversation = new Conversation();
        conversation.setId(channel.getSlackId());
        conversation.setName(channel.getName());

        ConversationsInfoResponse conversationsInfoResponse = new ConversationsInfoResponse();
        conversationsInfoResponse.setChannel(conversation);

        return conversationsInfoResponse;
    }
}
