package com.pickpick.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    ExtractableResponse<Response> post(String uri, Object object) {
        return RestAssured.given().log().all()
                .body(object)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(uri)
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> get(String uri) {
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> getWithAuth(String uri, Long memberId) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + memberId)
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }
}
