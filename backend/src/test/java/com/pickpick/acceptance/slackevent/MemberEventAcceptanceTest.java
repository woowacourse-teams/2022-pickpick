package com.pickpick.acceptance.slackevent;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.멤버_정보_수정;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.회원가입;

import com.pickpick.acceptance.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("멤버 관련 슬랙 이벤트 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
class MemberEventAcceptanceTest extends AcceptanceTest {

    private static final String MEMBER_SLACK_ID = "C00001234";

    @Test
    void 슬랙_워크스페이스에_신규_멤버가_참여하면_저장되어야_한다() {
        // given & when
        ExtractableResponse<Response> response = 회원가입(MEMBER_SLACK_ID);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 멤버_수정_발생_시_프로필_이미지와_이름이_업데이트_된다() {
        // given
        회원가입(MEMBER_SLACK_ID);

        // when
        ExtractableResponse<Response> response = 멤버_정보_수정(MEMBER_SLACK_ID, "실제이름", "표시이름", "test.png");

        // then
        상태코드_200_확인(response);
    }
}
