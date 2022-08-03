package com.pickpick.auth.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno",
            60000);

    @DisplayName("토큰 생성 후 값 추출")
    @Test
    void createToken() {
        // given
        long memberId = 1L;
        String token = jwtTokenProvider.createToken(String.valueOf(memberId));

        // when
        String actual = jwtTokenProvider.getPayload(token);

        // then
        assertThat(Long.parseLong(actual)).isEqualTo(memberId);
    }
}
