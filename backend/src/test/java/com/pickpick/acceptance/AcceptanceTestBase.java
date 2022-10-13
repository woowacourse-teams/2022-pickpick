package com.pickpick.acceptance;

import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.support.ExternalClient;
import com.pickpick.support.TestConfig;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import io.restassured.RestAssured;
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
    protected ExternalClient externalClient;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private WorkspaceRepository workspaces;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    protected Workspace saveWorkspace(final Workspace workspace) {
        return workspaces.save(workspace);
    }
}