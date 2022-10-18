package com.pickpick.acceptance.auth;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_400_확인;
import static com.pickpick.acceptance.RestHandler.에러코드_확인;
import static com.pickpick.acceptance.auth.AuthRestHandler.워크스페이스_초기화_및_로그인;
import static com.pickpick.acceptance.auth.AuthRestHandler.토큰_검증;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTestBase;
import com.pickpick.auth.support.JwtTokenProvider;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("인증 & 인가 인수 테스트")
class AuthAcceptanceTest extends AcceptanceTestBase {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Test
    void 정상_로그인() {
        // given
        //String memberSlackId = MemberFixture.createFirst().getSlackId();
        String memberCode = 슬랙에서_멤버의_코드_발행();

        // when
        ExtractableResponse<Response> response = 워크스페이스_초기화_및_로그인(memberCode);

        // then
        상태코드_200_확인(response);
        응답_바디에_토큰_존재(response);
    }

    @Test
    void 유효한_토큰_검증() {
        // given
        String token = jwtTokenProvider.createToken("2");

        // when
        ExtractableResponse<Response> response = 토큰_검증(token);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 유효하지_않은_토큰_검증_시_예외_처리() {
        // given
        String invalidToken = "abcde12345";

        // when
        ExtractableResponse<Response> response = 토큰_검증(invalidToken);

        // then
        상태코드_400_확인(response);
        에러코드_확인(response, "INVALID_TOKEN");
    }

    @Test
    void 만료된_토큰_검증_시_예외_처리() {
        // given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey, 0);
        String invalidToken = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 토큰_검증(invalidToken);

        // then
        상태코드_400_확인(response);
    }

    @Test
    void 시그니처가_다른_토큰_검증_시_예외_처리() {
        // given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("other" + secretKey, 60000);
        String invalidToken = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 토큰_검증(invalidToken);

        // then
        상태코드_400_확인(response);
        에러코드_확인(response, "INVALID_TOKEN");
    }

    private void 응답_바디에_토큰_존재(final ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("token")).isNotBlank();
    }
}
