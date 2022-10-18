package com.pickpick.acceptance;

import static com.pickpick.fixture.ChannelFixture.createAllChannels;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;

import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.channel.domain.Channel;
import com.pickpick.fixture.StubSlack;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.support.ExternalClient;
import com.pickpick.workspace.domain.Workspace;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTestBase {

    @LocalServerPort
    int port;

    @Autowired
    protected StubSlack slack;

    @Autowired
    protected ExternalClient externalClient;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    protected void 슬랙에서_멤버가_줍줍의_모든_채널에_참여(final String memberCode) {
        List<Channel> participatingChannels = createAllChannels(JUPJUP.create());
        slack.setParticipatingChannel(memberCode, participatingChannels);
    }

    protected String 슬랙에서_멤버의_코드_발행() {
        return slack.getRandomMemberCode();
    }

    protected String 로그인_응답에서_토큰_추출(final ExtractableResponse<Response> loginResponse) {
        return loginResponse.jsonPath().get("token");
    }

    protected String 코드로_멤버의_SlackId_추출(final String memberCode) {
        return slack.getMemberSlackIdByCode(memberCode);
    }

    protected Workspace 슬랙에서_멤버의_워크스페이스_정보_호출(final String memberCode) {
        return externalClient.callWorkspaceInfo(memberCode).toEntity();
    }
}
