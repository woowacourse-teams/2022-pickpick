package com.pickpick.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.config.dto.ErrorResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@SuppressWarnings("NonAsciiCharacters")
@Component
public class RestHandler {

    private static boolean showLog;

    @Value("${log}")
    public void setShowLog(boolean value) {
        showLog = value;
    }

    public static ExtractableResponse<Response> post(final String uri, final Object object) {
        return request(given -> given
                .body(object)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(uri)
        );
    }

    public static ExtractableResponse<Response> postWithToken(final String uri, final Object object,
                                                              final String token) {
        return request(given -> given
                .header("Authorization", "Bearer " + token)
                .body(object)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(uri)
        );
    }

    public static ExtractableResponse<Response> get(final String uri) {
        return get(uri, Map.of());
    }

    public static ExtractableResponse<Response> get(final String uri, final Map<String, Object> queryParams) {
        return request(given -> given
                .queryParams(queryParams)
                .when()
                .get(uri)
        );
    }

    public static ExtractableResponse<Response> getWithToken(final String uri, final String token) {
        return getWithToken(uri, token, Map.of());
    }

    public static ExtractableResponse<Response> getWithToken(final String uri, final String token,
                                                             final Map<String, Object> request) {
        return request(given -> given
                .queryParams(request)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(uri)
        );
    }

    public static ExtractableResponse<Response> putWithCreateToken(final String uri, final Object object,
                                                                   final String token) {
        return request(given -> given
                .header("Authorization", "Bearer " + token)
                .body(object)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
        );
    }

    public static ExtractableResponse<Response> deleteWithCreateToken(final String uri, final String token) {
        return request(given -> given
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(uri)
        );
    }

    private static ExtractableResponse<Response> request(Function<RequestSpecification, Response> function) {
        if (showLog) {
            RequestSpecification given = RestAssured.given().log().all();
            return function.apply(given)
                    .then()
                    .log().all()
                    .extract();
        }

        RequestSpecification given = RestAssured.given();
        return function.apply(given)
                .then()
                .extract();
    }

    public static void 상태코드_200_확인(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상태코드_400_확인(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 상태코드_확인(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    public static void 에러코드_확인(final ExtractableResponse<Response> response, final String errorCode) {
        String responseErrorCode = response.jsonPath().getObject("", ErrorResponse.class).getCode();
        assertThat(responseErrorCode).isEqualTo(errorCode);
    }
}
