package com.pickpick.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.controller.dto.ChannelResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("채널 기능")
@SuppressWarnings("NonAsciiCharacters")
public class ChannelAcceptanceTest extends AcceptanceTest {

    @Test
    void 유저_전체_채널_목록_조회() {
        List<ChannelResponse> list = getWithAuth("/api/channels", 2L)
                .jsonPath()
                .getList("channels.", ChannelResponse.class);

        assertThat(list.size()).isEqualTo(4);
    }
}
