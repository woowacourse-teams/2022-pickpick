package com.pickpick.acceptance.slackevent;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.URL_검증;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_삭제;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_수정;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_전송;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.브로드캐스트_메시지_전송;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.회원가입;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.slackevent.application.SlackEvent;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메시지 관련 슬랙 이벤트 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
class MessageEventAcceptanceTest extends AcceptanceTest {

    private static final String MEMBER_SLACK_ID = "U12345";
    private static final String MESSAGE_SLACK_ID = UUID.randomUUID().toString();

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
        회원가입(MEMBER_SLACK_ID);

        // when
        ExtractableResponse<Response> response = 메시지_전송(MEMBER_SLACK_ID, MESSAGE_SLACK_ID, "");

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 메시지_수정_요청_시_메시지_내용과_수정_시간이_업데이트_된다() {
        // given
        회원가입(MEMBER_SLACK_ID);
        메시지_전송(MEMBER_SLACK_ID, MESSAGE_SLACK_ID, "");

        // when
        ExtractableResponse<Response> response = 메시지_수정(MEMBER_SLACK_ID, MESSAGE_SLACK_ID);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 메시지_삭제_요청_시_메시지가_삭제_된다() {
        // given
        회원가입(MEMBER_SLACK_ID);

        // when
        ExtractableResponse<Response> response = 메시지_삭제(MEMBER_SLACK_ID, MESSAGE_SLACK_ID);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 스레드를_작성하면서_바로_채널로_전송_시_메시지가_저장된다() {
        // given
        회원가입(MEMBER_SLACK_ID);

        // when
        ExtractableResponse<Response> response = 메시지_전송(MEMBER_SLACK_ID, MESSAGE_SLACK_ID,
                SlackEvent.MESSAGE_THREAD_BROADCAST.getSubtype());

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 스레드_작성_후_메뉴에서_채널로_전송_시_메시지가_저장된다() {
        // given
        회원가입(MEMBER_SLACK_ID);

        // when
        ExtractableResponse<Response> response = 브로드캐스트_메시지_전송(MEMBER_SLACK_ID);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 파일_공유_메시지_요청_시_메시지가_저장된다() {
        // given
        회원가입(MEMBER_SLACK_ID);

        // when
        ExtractableResponse<Response> response = 메시지_전송(MEMBER_SLACK_ID, MESSAGE_SLACK_ID,
                SlackEvent.MESSAGE_FILE_SHARE.getSubtype());

        // then
        상태코드_200_확인(response);
    }
}
