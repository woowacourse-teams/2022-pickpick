package com.pickpick.acceptance.slackevent;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.auth.AuthRestHandler.워크스페이스_초기화_및_로그인;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.채널_삭제;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.채널_생성;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.채널_이름_변경;

import com.pickpick.acceptance.AcceptanceTestBase;
import com.pickpick.channel.domain.Channel;
import com.pickpick.fixture.ChannelFixture;
import com.pickpick.fixture.MemberFixture;
import com.pickpick.workspace.domain.Workspace;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("채널 관련 슬랙 이벤트 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
class ChannelEventAcceptanceTest extends AcceptanceTestBase {

    private Workspace workspace;

    @BeforeEach
    void init() {
        String memberSlackId = MemberFixture.createFirst().getSlackId();
        워크스페이스_초기화_및_로그인(memberSlackId);
        workspace = externalClient.callBotInfo(memberSlackId).toEntity();
    }

    @Test
    void 새로운_채널_생성_시_저장() {
        // given
        Channel channel = ChannelFixture.NEW_CHANNEL.create();

        // when
        ExtractableResponse<Response> response = 채널_생성(workspace, channel);

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 기존_채널_이름_변경_시_반영() {
        // given
        Channel channel = ChannelFixture.NEW_CHANNEL.create();
        채널_생성(workspace, channel);

        // when
        ExtractableResponse<Response> response = 채널_이름_변경(workspace, channel, "채널 새 이름");

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 채널_삭제_확인() {
        // given
        Channel channel = ChannelFixture.NEW_CHANNEL.create();
        채널_생성(workspace, channel);

        // when
        ExtractableResponse<Response> response = 채널_삭제(channel);

        // then
        상태코드_200_확인(response);
    }
}
