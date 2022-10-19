package com.pickpick.acceptance.channel;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.auth.AuthRestHandler.워크스페이스_초기화_및_로그인;
import static com.pickpick.acceptance.channel.ChannelRestHandler.유저_전체_채널_목록_조회_요청;
import static com.pickpick.fixture.MemberFixture.YEONLOG;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTestBase;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.fixture.ChannelFixture;
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
        String code = 슬랙에서_멤버의_코드_발행(YEONLOG);
        ExtractableResponse<Response> loginResponse = 워크스페이스_초기화_및_로그인(code);
        String token = 로그인_응답에서_토큰_추출(loginResponse);

        // when
        ExtractableResponse<Response> response = 유저_전체_채널_목록_조회_요청(token);

        // then
        List<ChannelResponse> channels = response.jsonPath().getList("channels.", ChannelResponse.class);

        상태코드_200_확인(response);
        assertThat(channels).hasSize(ChannelFixture.getDefaultSize());
    }
}
