package com.pickpick.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.support.DatabaseCleaner;
import com.slack.api.methods.MethodsClient;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.Clock;
import java.util.Map;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @SpyBean
    protected Clock clock;

    @MockBean
    protected MethodsClient slackClient;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Value("${log}")
    private boolean showLog;

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

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    protected ExtractableResponse<Response> post(final String uri, final Object object) {
        return request(given -> given
                .body(object)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(uri)
        );
    }

    protected ExtractableResponse<Response> postWithCreateToken(final String uri, final Object object,
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

    protected ExtractableResponse<Response> get(final String uri) {
        return request(given -> given
                .when()
                .get(uri)
        );
    }

    protected ExtractableResponse<Response> get(final String uri, final Map<String, Object> queryParams) {
        return request(given -> given
                .queryParams(queryParams)
                .log().all()
                .when()
                .get(uri)
        );
    }

    protected ExtractableResponse<Response> getWithToken(final String uri, final String token) {
        return request(given -> given
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get(uri)
        );
    }

    protected ExtractableResponse<Response> getWithCreateToken(final String uri, final Long memberId) {
        String token = createToken(memberId);

        return request(given -> given
                .header("Authorization", "Bearer " + token)
                .when()
                .get(uri)
        );
    }

    protected ExtractableResponse<Response> getWithCreateToken(final String uri, final Long memberId,
                                                               final Map<String, Object> request) {
        String token = createToken(memberId);

        return request(given -> given
                .queryParams(request)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(uri)
        );
    }

    protected ExtractableResponse<Response> putWithCreateToken(final String uri, final Object object,
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

    protected ExtractableResponse<Response> deleteWithCreateToken(final String uri, final Long memberId) {
        String token = createToken(memberId);

        return request(given -> given
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(uri)
        );
    }

    private String createToken(final Long memberId) {
        return jwtTokenProvider.createToken(String.valueOf(memberId));
    }

    protected void 상태코드_200_확인(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    protected void 상태코드_400_확인(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    protected void 상태코드_확인(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}
