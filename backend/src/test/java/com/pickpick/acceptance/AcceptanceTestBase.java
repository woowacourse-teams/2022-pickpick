package com.pickpick.acceptance;

import static com.pickpick.fixture.ChannelFixture.createAllChannels;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;

import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.channel.domain.Channel;
import com.pickpick.fixture.FakeClientFixture;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.support.ExternalClient;
import com.pickpick.support.TestConfig;
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
import org.springframework.context.annotation.Import;

@Import(value = TestConfig.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTestBase {

    @LocalServerPort
    int port;

    @Autowired
    protected FakeClientFixture slack;

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
        return FakeClientFixture.getRandomMemberCode();
    }

    protected String 로그인_응답에서_토큰_추출(final ExtractableResponse<Response> loginResponse) {
        return loginResponse.jsonPath().get("token");
    }
}
