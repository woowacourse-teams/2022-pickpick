package com.pickpick.acceptance.channel;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.auth.AuthRestHandler.워크스페이스_초기화_및_로그인;
import static com.pickpick.acceptance.channel.ChannelRestHandler.유저_전체_채널_목록_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTestBase;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.fixture.FakeClientFixture;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("채널 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
class ChannelAcceptanceTest extends AcceptanceTestBase {


    @Test
    void 유저_전체_채널_목록_조회() {
        // given
        String memberCode = FakeClientFixture.getRandomMemberCode();
        ExtractableResponse<Response> loginResponse = 워크스페이스_초기화_및_로그인(memberCode);
        String token = loginResponse.jsonPath().get("token");

        // when
        ExtractableResponse<Response> response = 유저_전체_채널_목록_조회_요청(token);

        // then
        List<ChannelResponse> channels = response.jsonPath().getList("channels.", ChannelResponse.class);

        상태코드_200_확인(response);
        assertThat(channels).hasSize(FakeClientFixture.getDefaultChannelSize());
    }
}
