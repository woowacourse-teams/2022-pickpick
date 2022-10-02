package com.pickpick.acceptance.channel;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.channel.ChannelRestHandler.유저_전체_채널_목록_조회_요청;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.채널_생성;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.회원가입;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.fixture.ChannelFixtures;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("채널 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
public class ChannelAcceptanceTest extends AcceptanceTest {

    @Test
    void 유저_전체_채널_목록_조회() {
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

    private void 채널_목록_생성(final String memberSlackId) {
        for (Channel channel : ChannelFixtures.allChannels()) {
            채널_생성(memberSlackId, channel, slackClient);
        }
    }
}
