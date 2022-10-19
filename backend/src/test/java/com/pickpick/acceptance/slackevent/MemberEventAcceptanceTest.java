package com.pickpick.acceptance.slackevent;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.auth.AuthRestHandler.워크스페이스_초기화_및_로그인;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.멤버_정보_수정;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.회원가입;
import static com.pickpick.fixture.MemberFixture.BOM;

import com.pickpick.acceptance.AcceptanceTestBase;
import com.pickpick.workspace.domain.Workspace;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("멤버 관련 슬랙 이벤트 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
class MemberEventAcceptanceTest extends AcceptanceTestBase {

    private String memberSlackId;
    private Workspace workspace;

    @BeforeEach
    void init() {
        String memberCode = 슬랙에서_멤버의_코드_발행(BOM);
        워크스페이스_초기화_및_로그인(memberCode);

        memberSlackId = 코드로_멤버의_SlackId_추출(memberCode);
        workspace = 슬랙에서_멤버의_워크스페이스_정보_호출(memberCode);
    }

    @Test
    void 슬랙_워크스페이스에_신규_멤버가_참여하면_저장되어야_한다() {
        // given & when
        ExtractableResponse<Response> response = 회원가입("new" + memberSlackId, workspace.getSlackId());

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 멤버_수정_발생_시_프로필_이미지와_이름이_업데이트_된다() {
        // given
        회원가입(memberSlackId, workspace.getSlackId());

        // when
        ExtractableResponse<Response> response = 멤버_정보_수정(memberSlackId, "실제이름", "표시이름", "test.png");

        // then
        상태코드_200_확인(response);
    }
}
