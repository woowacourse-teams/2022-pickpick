package com.pickpick.auth.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pickpick.exception.auth.ExpiredTokenException;
import com.pickpick.exception.auth.InvalidTokenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno",
            60000);

    @DisplayName("토큰 생성 후 값 추출")
    @Test
    void getPayload() {
        // given
        long memberId = 1L;
        String token = jwtTokenProvider.createToken(String.valueOf(memberId));

        // when
        String actual = jwtTokenProvider.getPayload(token);

        // then
        assertThat(Long.parseLong(actual)).isEqualTo(memberId);
    }

    @DisplayName("토큰 유효 시간 검증")
    @Test
    void validateExpiredToken() {
        // given
        long memberId = 1L;
        JwtTokenProvider expiredTokenProvider = new JwtTokenProvider(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno",
                0);
        String token = expiredTokenProvider.createToken(String.valueOf(memberId));

        // when & then
        assertThatThrownBy(() -> expiredTokenProvider.validateToken(token))
                .isInstanceOf(ExpiredTokenException.class);
    }

    @DisplayName("유효하지 않은 토큰 검증")
    @Test
    void validateInvalidToken() {
        // given
        String token = "fake_token";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(token))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("다른 시그니쳐로 생성된 토큰 검증")
    @Test
    void validateInvalidSignature() {
        // given
        JwtTokenProvider otherJwtTokenProvider = new JwtTokenProvider("secretKey12345678912356714252637", 60000);
        Long memberId = 1L;
        String token = otherJwtTokenProvider.createToken(String.valueOf(memberId));

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(token))
                .isInstanceOf(InvalidTokenException.class);
    }
}
