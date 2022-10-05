package com.pickpick.acceptance.message;

import static com.pickpick.acceptance.RestHandler.deleteWithToken;
import static com.pickpick.acceptance.RestHandler.getWithToken;
import static com.pickpick.acceptance.RestHandler.postWithToken;
import static com.pickpick.acceptance.RestHandler.putWithToken;
import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_확인;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.message.ui.dto.ReminderResponse;
import com.pickpick.message.ui.dto.ReminderResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/reminder.sql"})
@DisplayName("리마인더 기능")
@SuppressWarnings("NonAsciiCharacters")
public class ReminderAcceptanceTest extends AcceptanceTest {

    private static final String REMINDER_API_URL = "/api/reminders";

    @Test
    void 리마인더_생성() {
        // given & when
        ExtractableResponse<Response> response = postWithToken(REMINDER_API_URL,
                Map.of("messageId", 1, "reminderDate", "2022-08-10T19:21:55"),
                jwtTokenProvider.createToken("1"));

        // then
        상태코드_확인(response, HttpStatus.CREATED);
    }

    @Test
    void 리마인더_단건_조회_정상_응답() {
        // given
        Map<String, Object> request = Map.of("messageId", "1");
        String token = jwtTokenProvider.createToken("2");

        // when
        ExtractableResponse<Response> response = getWithToken(REMINDER_API_URL, token, request);

        // then
        상태코드_200_확인(response);
        ReminderResponse reminderResponse = response.jsonPath().getObject("", ReminderResponse.class);
        assertThat(reminderResponse.getId()).isEqualTo(1L);
    }

    @Test
    void 존재하지_않는_리마인더_조회시_404_응답() {
        // given
        Map<String, Object> request = Map.of("messageId", "100");
        String token = jwtTokenProvider.createToken("2");

        // when
        ExtractableResponse<Response> response = getWithToken(REMINDER_API_URL, token, request);

        // then
        상태코드_확인(response, HttpStatus.NOT_FOUND);
    }

    @Test
    void 멤버_ID_2번으로_리마인더_목록_조회() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        Map<String, Object> request = Map.of("reminderId", "");
        List<Long> expectedIds = List.of(1L);
        boolean expectedHasPast = false;
        String token = jwtTokenProvider.createToken("2");

        // when
        ExtractableResponse<Response> response = getWithToken(REMINDER_API_URL, token, request);

        // then
        상태코드_확인(response, HttpStatus.OK);

        ReminderResponses reminderResponses = response.jsonPath().getObject("", ReminderResponses.class);
        assertAll(
                () -> assertThat(reminderResponses.hasFuture()).isEqualTo(expectedHasPast),
                () -> assertThat(convertToIds(reminderResponses)).containsExactlyElementsOf(expectedIds)
        );
    }

    @Test
    void 멤버_ID_1번이고_리마인더_ID_10번일_때_리마인더_목록_조회() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        Map<String, Object> request = Map.of("reminderId", "10");
        List<Long> expectedIds = List.of(11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L, 23L);
        boolean expectedHasPast = false;
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = getWithToken(REMINDER_API_URL, token, request);

        // then
        상태코드_확인(response, HttpStatus.OK);

        ReminderResponses reminderResponses = response.jsonPath().getObject("", ReminderResponses.class);
        assertAll(
                () -> assertThat(reminderResponses.hasFuture()).isEqualTo(expectedHasPast),
                () -> assertThat(convertToIds(reminderResponses)).containsExactlyElementsOf(expectedIds)
        );
    }

    @Test
    void 리마인더_조회_시_가장_최신인_리마인더가_포함된다면_hasFuture가_False다() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        Map<String, Object> request = Map.of("reminderId", "");
        List<Long> expectedIds = List.of(1L);
        boolean expectedHasPast = false;
        String token = jwtTokenProvider.createToken("2");

        // when
        ExtractableResponse<Response> response = getWithToken(REMINDER_API_URL, token, request);

        // then
        상태코드_확인(response, HttpStatus.OK);

        ReminderResponses reminderResponses = response.jsonPath().getObject("", ReminderResponses.class);
        assertAll(
                () -> assertThat(reminderResponses.hasFuture()).isEqualTo(expectedHasPast),
                () -> assertThat(convertToIds(reminderResponses)).containsExactlyElementsOf(expectedIds)
        );
    }

    @Test
    void 리마인더_조회_시_가장_최신인_리마인더가_포함되지_않는다면_hasFuture가_True다() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        Map<String, Object> request = Map.of("reminderId", "2");
        List<Long> expectedIds = List.of(
                3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L);
        boolean expectedHasPast = true;
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = getWithToken(REMINDER_API_URL, token, request);

        // then
        상태코드_확인(response, HttpStatus.OK);

        ReminderResponses reminderResponses = response.jsonPath().getObject("", ReminderResponses.class);
        assertAll(
                () -> assertThat(reminderResponses.hasFuture()).isEqualTo(expectedHasPast),
                () -> assertThat(convertToIds(reminderResponses)).containsExactlyElementsOf(expectedIds)
        );
    }

    private List<Long> convertToIds(final ReminderResponses response) {
        return response.getReminders()
                .stream()
                .map(ReminderResponse::getId)
                .collect(Collectors.toList());
    }

    @Test
    void 리마인더_조회_시_count_값이_없으면_20개가_조회된다() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        Map<String, Object> request = Map.of("reminderId", "");
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = getWithToken(REMINDER_API_URL, token, request);

        // then
        상태코드_확인(response, HttpStatus.OK);

        int size = response.jsonPath()
                .getObject("", ReminderResponses.class)
                .getReminders()
                .size();

        assertThat(size).isEqualTo(20);
    }

    @Test
    void 리마인더_조회_시_count_값이_있다면_count_개수_만큼_조회된다() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        int count = 10;
        Map<String, Object> request = Map.of("reminderId", "", "count", count);
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = getWithToken(REMINDER_API_URL, token, request);

        // then
        상태코드_확인(response, HttpStatus.OK);

        int size = response.jsonPath()
                .getObject("", ReminderResponses.class)
                .getReminders()
                .size();

        assertThat(size).isEqualTo(count);
    }

    @Test
    void 리마인더_정상_수정() {
        // given
        Map<String, Object> request = Map.of("messageId", "2", "reminderDate", LocalDateTime.now().toString());
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = putWithToken(REMINDER_API_URL, request, token);

        // then
        상태코드_확인(response, HttpStatus.OK);
    }

    @Test
    void 사용자에게_존재하지_않는_리마인더_수정() {
        // given
        Map<String, Object> request = Map.of("messageId", "1", "reminderDate", LocalDateTime.now().toString());
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = putWithToken(REMINDER_API_URL, request, token);

        // then
        상태코드_확인(response, HttpStatus.BAD_REQUEST);
    }

    @Test
    void 리마인더_정상_삭제() {
        // given
        long messageId = 1L;
        String token = jwtTokenProvider.createToken("2");

        // when
        ExtractableResponse<Response> response = deleteWithToken(
                REMINDER_API_URL,
                token, Map.of("messageId", messageId));

        // then
        상태코드_확인(response, HttpStatus.NO_CONTENT);
    }

    @Test
    void 사용자에게_존재하지_않는_리마인더_삭제() {
        // given
        long messageId = 1L;
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = deleteWithToken(
                REMINDER_API_URL,
                token, Map.of("messageId", messageId));

        // then
        상태코드_확인(response, HttpStatus.BAD_REQUEST);
    }
}
