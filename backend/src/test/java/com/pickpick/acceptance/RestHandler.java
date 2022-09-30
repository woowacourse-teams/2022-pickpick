package com.pickpick.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.auth.support.JwtTokenProvider;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@SuppressWarnings("NonAsciiCharacters")
@Component
public class RestHandler {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${log}")
    private boolean showLog;

    public ExtractableResponse<Response> post(final String uri, final Object object) {
        return request(given -> given
                .body(object)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(uri)
        );
    }

    public ExtractableResponse<Response> postWithCreateToken(final String uri, final Object object,
                                                             final Long memberId) {
        String token = createToken(memberId);

        return request(given -> given
                .header("Authorization", "Bearer " + token)
                .body(object)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(uri)
        );
    }

    public ExtractableResponse<Response> get(final String uri) {
        return request(given -> given
                .when()
                .get(uri)
        );
    }

    public ExtractableResponse<Response> get(final String uri, final Map<String, Object> queryParams) {
        return request(given -> given
                .queryParams(queryParams)
                .log().all()
                .when()
                .get(uri)
        );
    }

    public ExtractableResponse<Response> getWithToken(final String uri, final String token) {
        return request(given -> given
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get(uri)
        );
    }

    public ExtractableResponse<Response> getWithCreateToken(final String uri, final Long memberId) {
        String token = createToken(memberId);

        return request(given -> given
                .header("Authorization", "Bearer " + token)
                .when()
                .get(uri)
        );
    }

    public ExtractableResponse<Response> getWithCreateToken(final String uri, final Long memberId,
                                                            final Map<String, Object> request) {
        String token = createToken(memberId);

        return request(given -> given
                .queryParams(request)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(uri)
        );
    }

    public ExtractableResponse<Response> putWithCreateToken(final String uri, final Object object,
                                                            final Long memberId) {
        String token = createToken(memberId);

        return request(given -> given
                .header("Authorization", "Bearer " + token)
                .body(object)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
        );
    }

    public ExtractableResponse<Response> deleteWithCreateToken(final String uri, final Long memberId) {
        String token = createToken(memberId);

        return request(given -> given
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(uri)
        );
    }

    private ExtractableResponse<Response> request(Function<RequestSpecification, Response> function) {
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

    private String createToken(final Long memberId) {
        return jwtTokenProvider.createToken(String.valueOf(memberId));
    }

    public void 상태코드_200_확인(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public void 상태코드_400_확인(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public void 상태코드_확인(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}
