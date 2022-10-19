package com.pickpick.acceptance.slackevent;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.auth.AuthRestHandler.워크스페이스_초기화_및_로그인;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.URL_검증;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_삭제;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_수정;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_전송;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.브로드캐스트_메시지_전송;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.회원가입;
import static com.pickpick.fixture.MemberFixture.BOM;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTestBase;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.workspace.domain.Workspace;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메시지 관련 슬랙 이벤트 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
class MessageEventAcceptanceTest extends AcceptanceTestBase {

    private static final String MESSAGE_SLACK_ID = UUID.randomUUID().toString();

    private Workspace workspace;
    private String memberSlackId;

    @BeforeEach
    void init() {
        String code = 슬랙에서_코드_발행(BOM);
        워크스페이스_초기화_및_로그인(code);
        memberSlackId = 코드로_멤버의_SlackId_추출(code);
        workspace = 슬랙에서_멤버의_워크스페이스_정보_호출(code);
    }

    @Test
    void URL_검증_요청_시_challenge_를_응답한다() {
        // given
        String challenge = "challenge";

        // when
        ExtractableResponse<Response> response = URL_검증("token", "url_verification", challenge);

        // then
        상태코드_200_확인(response);
        assertThat(response.asString()).isEqualTo(challenge);
    }

    @Test
    void 메시지_저장_성공() {
        // given
        회원가입(memberSlackId, workspace.getSlackId());

        // when
        ExtractableResponse<Response> response = 메시지_전송(memberSlackId, MESSAGE_SLACK_ID, "");

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 메시지_수정_요청_시_메시지_내용과_수정_시간이_업데이트_된다() {
        // given
        회원가입(memberSlackId, workspace.getSlackId());
        메시지_전송(memberSlackId, MESSAGE_SLACK_ID, "");

        // when
        ExtractableResponse<Response> response = 메시지_수정(memberSlackId, MESSAGE_SLACK_ID);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 메시지_삭제_요청_시_메시지가_삭제_된다() {
        // given
        회원가입(memberSlackId, workspace.getSlackId());

        // when
        ExtractableResponse<Response> response = 메시지_삭제(memberSlackId, MESSAGE_SLACK_ID);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 스레드를_작성하면서_바로_채널로_전송_시_메시지가_저장된다() {
        // given
        회원가입(memberSlackId, workspace.getSlackId());

        // when
        ExtractableResponse<Response> response = 메시지_전송(memberSlackId, MESSAGE_SLACK_ID,
                SlackEvent.MESSAGE_THREAD_BROADCAST.getSubtype());

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 스레드_작성_후_메뉴에서_채널로_전송_시_메시지가_저장된다() {
        // given
        회원가입(memberSlackId, workspace.getSlackId());

        // when
        ExtractableResponse<Response> response = 브로드캐스트_메시지_전송(memberSlackId);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 파일_공유_메시지_요청_시_메시지가_저장된다() {
        // given
        회원가입(memberSlackId, workspace.getSlackId());

        // when
        ExtractableResponse<Response> response = 메시지_전송(memberSlackId, MESSAGE_SLACK_ID,
                SlackEvent.MESSAGE_FILE_SHARE.getSubtype());

        // then
        상태코드_200_확인(response);
    }
}
