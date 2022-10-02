package com.pickpick.acceptance.message;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_확인;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_단건_조회;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_목록_조회;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_삭제;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_생성;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_수정;
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
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/reminder.sql"})
@DisplayName("리마인더 기능")
@SuppressWarnings("NonAsciiCharacters")
public class ReminderAcceptanceTest extends AcceptanceTest {

    @Test
    void 리마인더_생성_검증() {
        // given
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 리마인더_생성(token, 1, LocalDateTime.of(2022, 8, 10, 19, 21, 55));

        // then
        상태코드_확인(response, HttpStatus.CREATED);
    }

    @Test
    void 리마인더_단건_조회_정상_응답() {
        // given
        String token = jwtTokenProvider.createToken("2");

        // when
        ExtractableResponse<Response> response = 리마인더_단건_조회(token, 1L);

        // then
        상태코드_200_확인(response);
        ReminderResponse reminderResponse = response.jsonPath().getObject("", ReminderResponse.class);
        assertThat(reminderResponse.getId()).isEqualTo(1L);
    }

    @Test
    void 존재하지_않는_리마인더_조회시_404_응답() {
        // given
        String token = jwtTokenProvider.createToken("2");

        // when
        ExtractableResponse<Response> response = 리마인더_단건_조회(token, 100L);

        // then
        상태코드_확인(response, HttpStatus.NOT_FOUND);
    }

    @Test
    void 멤버_ID_2번으로_리마인더_목록_조회() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        String token = jwtTokenProvider.createToken("2");
        List<Long> expectedIds = List.of(1L);
        boolean expectedHasPast = false;

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, null, null);

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

        String token = jwtTokenProvider.createToken("1");

        List<Long> expectedIds = List.of(11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L, 23L);
        boolean expectedHasPast = false;

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, 10L, null);

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

        String token = jwtTokenProvider.createToken("2");

        List<Long> expectedIds = List.of(1L);
        boolean expectedHasPast = false;

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, null, null);

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

        String token = jwtTokenProvider.createToken("1");

        List<Long> expectedIds = List.of(
                3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L);
        boolean expectedHasPast = true;

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, 2L, null);

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

        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, null, null);

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
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, null, count);

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
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 리마인더_수정(token, 2L, LocalDateTime.now());

        // then
        상태코드_확인(response, HttpStatus.OK);
    }

    @Test
    void 사용자에게_존재하지_않는_리마인더_수정() {
        // given
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 리마인더_수정(token, 1L, LocalDateTime.now());

        // then
        상태코드_확인(response, HttpStatus.BAD_REQUEST);
    }

    @Test
    void 리마인더_정상_삭제() {
        // given
        long messageId = 1L;
        String token = jwtTokenProvider.createToken("2");

        // when
        ExtractableResponse<Response> response = 리마인더_삭제(token, messageId);

        // then
        상태코드_확인(response, HttpStatus.NO_CONTENT);
    }

    @Test
    void 사용자에게_존재하지_않는_리마인더_삭제() {
        // given
        long messageId = 1L;
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 리마인더_삭제(token, messageId);

        // then
        상태코드_확인(response, HttpStatus.BAD_REQUEST);
    }
}
