package com.pickpick.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.auth.support.JwtTokenProvider;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    ExtractableResponse<Response> post(final String uri, final Object object) {
        return RestAssured.given().log().all()
                .body(object)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(uri)
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> postWithCreateToken(final String uri, final Object object, final Long memberId) {
        String token = createToken(memberId);

        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + token)
                .body(object)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(uri)
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> get(final String uri) {
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> get(final String uri, final Map<String, Object> queryParams) {
        return RestAssured.given()
                .queryParams(queryParams)
                .log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> getWithCreateToken(final String uri, final Long memberId) {
        String token = createToken(memberId);

        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> getWithCreateToken(final String uri, final Long memberId,
                                                     final Map<String, Object> request) {
        String token = createToken(memberId);

        return RestAssured.given().log().all()
                .queryParams(request)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> putWithCreateToken(final String uri, final Object object, final Long memberId) {
        String token = createToken(memberId);

        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + token)
                .body(object)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> deleteWithCreateToken(final String uri, final Long memberId) {
        String token = createToken(memberId);

        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    private String createToken(final Long memberId) {
        return jwtTokenProvider.createToken(String.valueOf(memberId));
    }

    void 상태코드_200_확인(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    void 상태코드_400_확인(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    void 상태코드_확인(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}
