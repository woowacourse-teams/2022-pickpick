package com.pickpick.acceptance;

import com.pickpick.controller.dto.ChannelResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("채널 기능")
@SuppressWarnings("NonAsciiCharacters")
public class ChannelAcceptanceTest extends AcceptanceTest {

    @Test
    void 전체_채널_목록_조회() {
        List<ChannelResponse> list = get("/api/channels")
                .jsonPath()
                .getList(".", ChannelResponse.class);

        assertThat(list.size()).isEqualTo(6);
    }
}
