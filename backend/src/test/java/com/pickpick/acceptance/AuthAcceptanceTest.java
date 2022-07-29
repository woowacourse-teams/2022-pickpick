package com.pickpick.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String API = "/api/slack-login";

}
