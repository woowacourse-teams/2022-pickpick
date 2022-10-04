package com.pickpick.acceptance.message;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_확인;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_단건_조회;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_목록_조회;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_삭제;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_생성;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_수정;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_목록_생성;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.채널_생성_후_메시지_저장;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.회원가입;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.fixture.ChannelFixture;
import com.pickpick.message.ui.dto.ReminderResponse;
import com.pickpick.message.ui.dto.ReminderResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("리마인더 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
public class ReminderAcceptanceTest extends AcceptanceTest {

    private static final String MEMBER_SLACK_ID = "MEM0001";

    @Test
    void 리마인더_생성_검증() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());

        // when
        ExtractableResponse<Response> response = 리마인더_생성(token, 1, LocalDateTime.now());

        // then
        상태코드_확인(response, HttpStatus.CREATED);
    }

    @Test
    void 리마인더_단건_조회_정상_응답() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());
        리마인더_생성(token, 1, LocalDateTime.now());

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
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());

        // when
        ExtractableResponse<Response> response = 리마인더_단건_조회(token, 1L);

        // then
        상태코드_확인(response, HttpStatus.NOT_FOUND);
    }

    @Test
    void 멤버_ID_1번으로_리마인더_목록_조회() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");

        int messageCount = 10;
        메시지_목록_생성(MEMBER_SLACK_ID, messageCount);

        List<Long> messageIdsForReminder = List.of(1L, 3L, 5L, 7L);
        리마인더_목록_생성(token, messageIdsForReminder);

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, null, null);

        // then
        상태코드_확인(response, HttpStatus.OK);

        ReminderResponses reminderResponses = response.jsonPath().getObject("", ReminderResponses.class);
        assertAll(
                () -> assertThat(reminderResponses.hasFuture()).isFalse(),
                () -> assertThat(convertToMessageIds(reminderResponses)).containsExactlyElementsOf(
                        messageIdsForReminder)
        );
    }

    @Test
    void 멤버_ID_1번이고_리마인더_ID_2번일_때_리마인더_목록_조회() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");

        int messageCount = 5;
        메시지_목록_생성(MEMBER_SLACK_ID, messageCount);

        List<Long> messageIdsForReminder = List.of(1L, 2L, 3L, 4L, 5L);
        리마인더_목록_생성(token, messageIdsForReminder);

        List<Long> expectedMessageIds = List.of(3L, 4L, 5L);

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, 2L, null);

        // then
        상태코드_확인(response, HttpStatus.OK);

        ReminderResponses reminderResponses = response.jsonPath().getObject("", ReminderResponses.class);
        assertAll(
                () -> assertThat(reminderResponses.hasFuture()).isFalse(),
                () -> assertThat(convertToMessageIds(reminderResponses)).containsExactlyElementsOf(expectedMessageIds)
        );
    }

    @Test
    void 리마인더_조회_시_가장_최신인_리마인더가_포함된다면_hasFuture가_False다() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");

        int messageCount = 11;
        메시지_목록_생성(MEMBER_SLACK_ID, messageCount);

        List<Long> messageIdsForReminder = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L);
        리마인더_목록_생성(token, messageIdsForReminder);

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, 3L, 10);

        // then
        상태코드_확인(response, HttpStatus.OK);

        ReminderResponses reminderResponses = response.jsonPath().getObject("", ReminderResponses.class);
        assertThat(reminderResponses.hasFuture()).isFalse();
    }

    @Test
    void 리마인더_조회_시_가장_최신인_리마인더가_포함되지_않는다면_hasFuture가_True다() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");

        int messageCount = 11;
        메시지_목록_생성(MEMBER_SLACK_ID, messageCount);

        List<Long> messageIdsForReminder = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L);
        리마인더_목록_생성(token, messageIdsForReminder);

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, null, 10);

        // then
        상태코드_확인(response, HttpStatus.OK);

        ReminderResponses reminderResponses = response.jsonPath().getObject("", ReminderResponses.class);
        assertThat(reminderResponses.hasFuture()).isTrue();
    }

    @Test
    void 리마인더_조회_시_count_값이_없으면_20개가_조회된다() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");

        int messageCount = 20;
        메시지_목록_생성(MEMBER_SLACK_ID, messageCount);

        List<Long> messageIdsForReminder = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L,
                16L, 17L, 18L, 19L, 20L);
        리마인더_목록_생성(token, messageIdsForReminder);

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, null, null);

        // then
        상태코드_확인(response, HttpStatus.OK);
        assertThat(리마인더_개수(response)).isEqualTo(20);
    }

    @Test
    void 리마인더_조회_시_count_값이_있다면_count_개수_만큼_조회된다() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");

        int messageCount = 20;
        메시지_목록_생성(MEMBER_SLACK_ID, messageCount);

        List<Long> messageIdsForReminder = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L,
                16L, 17L, 18L, 19L, 20L);
        리마인더_목록_생성(token, messageIdsForReminder);

        int reminderCount = 10;

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, null, reminderCount);

        // then
        상태코드_확인(response, HttpStatus.OK);
        assertThat(리마인더_개수(response)).isEqualTo(reminderCount);
    }

    @Test
    void 리마인더_정상_수정() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());

        long messageId = 1L;
        리마인더_생성(token, messageId, LocalDateTime.now().plusDays(1));

        // when
        ExtractableResponse<Response> response = 리마인더_수정(token, messageId, LocalDateTime.now());

        // then
        상태코드_확인(response, HttpStatus.OK);
    }

    @Test
    void 사용자에게_존재하지_않는_리마인더_수정() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token1 = jwtTokenProvider.createToken("1");

        회원가입(MEMBER_SLACK_ID + "2");
        String token2 = jwtTokenProvider.createToken("2");

        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());
        리마인더_생성(token1, 1, LocalDateTime.now().plusDays(1));

        // when
        ExtractableResponse<Response> response = 리마인더_수정(token2, 1L, LocalDateTime.now());

        // then
        상태코드_확인(response, HttpStatus.BAD_REQUEST);
    }

    @Test
    void 리마인더_정상_삭제() {
        // given
        회원가입(MEMBER_SLACK_ID);
        String token = jwtTokenProvider.createToken("1");
        채널_생성_후_메시지_저장(MEMBER_SLACK_ID, ChannelFixture.QNA.create());

        long messageId = 1L;
        리마인더_생성(token, messageId, LocalDateTime.now());

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

    private void 리마인더_목록_생성(final String token, final List<Long> messageIds) {
        int size = messageIds.size();
        for (int i = 0; i < size; i++) {
            리마인더_생성(token, messageIds.get(i), LocalDateTime.now().plusDays(i + 1));
        }
    }

    private List<Long> convertToMessageIds(final ReminderResponses response) {
        return response.getReminders()
                .stream()
                .map(ReminderResponse::getMessageId)
                .collect(Collectors.toList());
    }

    private int 리마인더_개수(final ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject("", ReminderResponses.class)
                .getReminders()
                .size();
    }
}
