package com.pickpick.acceptance.channel;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.channel.ChannelRestHandler.유저_전체_채널_목록_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.channel.ui.dto.ChannelResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/channel.sql"})
@DisplayName("채널 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
public class ChannelAcceptanceTest extends AcceptanceTest {

    @Test
    void 유저_전체_채널_목록_조회() {
        // given
        String token = jwtTokenProvider.createToken("2");

        // when
        ExtractableResponse<Response> response = 유저_전체_채널_목록_조회_요청(token);

        // then
        List<ChannelResponse> channels = response.jsonPath().getList("channels.", ChannelResponse.class);

        상태코드_200_확인(response);
        assertThat(channels).hasSize(6);
    }
}
