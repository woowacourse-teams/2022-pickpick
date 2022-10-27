package com.pickpick.acceptance;

import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.fixture.MemberFixture;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.support.ExternalClient;
import com.pickpick.workspace.domain.Workspace;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTestBase {

    @LocalServerPort
    int port;

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

    protected String 슬랙에서_코드_발행(MemberFixture memberFixture) {
        return memberFixture.getCode();
    }

    protected String 로그인_응답에서_토큰_추출(final ExtractableResponse<Response> loginResponse) {
        return loginResponse.jsonPath().get("token");
    }

    protected String 코드로_멤버의_slackId_추출(final String code) {
        return MemberFixture.getMemberSlackIdByCode(code);
    }

    protected Workspace 슬랙에서_멤버의_워크스페이스_정보_호출(final String code) {
        return externalClient.callOAuthAccessInfo(code).toEntity();
    }
}
