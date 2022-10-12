package com.pickpick.acceptance.slackevent;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.채널_생성;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.channel.domain.Channel;
import com.pickpick.fixture.ChannelFixture;
import com.pickpick.fixture.WorkspaceFixture;
import com.pickpick.workspace.domain.Workspace;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("채널 관련 슬랙 이벤트 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
public class ChannelEventAcceptanceTest extends AcceptanceTest {

    @Test
    void 채널_생성_확인() {
        // given
        Workspace workspace = saveWorkspace(WorkspaceFixture.JUPJUP.create());
        Channel channel = ChannelFixture.NEW_CHANNEL.create();

        // when
        ExtractableResponse<Response> response = 채널_생성(workspace, channel);

        // then
        상태코드_200_확인(response);
    }
}
