package com.pickpick.acceptance.channel;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.auth.AuthRestHandler.워크스페이스_초기화_및_로그인;
import static com.pickpick.acceptance.channel.ChannelRestHandler.유저_전체_채널_목록_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTestBase;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.fixture.ChannelFixture;
import com.pickpick.fixture.MemberFixture;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("채널 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
class ChannelAcceptanceTest extends AcceptanceTestBase {

    private static final String MEMBER_SLACK_ID = MemberFixture.findFirst().getSlackId();

    @Test
    void 유저_전체_채널_목록_조회() {
        // given
        워크스페이스_초기화_및_로그인(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 유저_전체_채널_목록_조회_요청(token);

        // then
        List<ChannelResponse> channels = response.jsonPath().getList("channels.", ChannelResponse.class);

        상태코드_200_확인(response);
        assertThat(channels).hasSize(ChannelFixture.getDefaultSize());
    }
}
