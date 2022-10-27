package com.pickpick.acceptance.workspace;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_400_확인;
import static com.pickpick.acceptance.RestHandler.에러코드_확인;
import static com.pickpick.acceptance.workspace.WorkspaceRestHandler.워크스페이스_초기화;
import static com.pickpick.fixture.MemberFixture.BOM;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTestBase;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("워크스페이스 인수 테스트")
class WorkspaceAcceptanceTest extends AcceptanceTestBase {

    @Test
    void 정상_워크_스페이스_등록_후_로그인() {
        // given
        String code = 슬랙에서_코드_발행(BOM);

        // when
        ExtractableResponse<Response> response = 워크스페이스_초기화(code);

        // then
        상태코드_200_확인(response);
        응답_바디에_토큰_존재(response);
    }

    @Test
    void 워크스페이스_등록_후_워크스페이스_재등록시_예외처리() {
        // given
        String codeForInit = 슬랙에서_코드_발행(BOM);
        워크스페이스_초기화(codeForInit);

        // when
        String codeForLogin = 슬랙에서_코드_발행(BOM);
        ExtractableResponse<Response> response = 워크스페이스_초기화(codeForLogin);

        // then
        상태코드_400_확인(response);
        에러코드_확인(response, "WORKSPACE_DUPLICATE");
    }

    private void 응답_바디에_토큰_존재(final ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("token")).isNotBlank();
    }
}
