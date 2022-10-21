package com.pickpick.message.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SlackIdExtractorTest {

    private static final String MENTION_PREFIX = "<@";
    private static final String MENTION_SUFFIX = ">";

    private final SlackIdExtractor slackIdExtractor = new SlackIdExtractor();

    @DisplayName("슬랙 아이디가 한 개도 존재하지 않는 경우 빈 set 반환")
    @Test
    void noSlackId() {
        String text = "아무도 멘션하지 않은 메시지 내용";
        Set<String> slackIds = slackIdExtractor.extract(text);

        assertThat(slackIds).isEmpty();
    }

    @DisplayName("SlackId가 한 개 존재하는 경우 SlackId 한 개를 추출한다.")
    @Test
    void extractOneSlackId() {
        String text = "봄을 멘션 <@U1111111111> 한 메시지 내용";
        Set<String> slackIds = slackIdExtractor.extract(text);

        assertAll(
                () -> assertThat(slackIds).hasSize(1),
                () -> assertThat(slackIds).contains("U1111111111")
        );
    }

    @DisplayName("같은 SlackId가 여러 개 존재하는 경우 SlackId 한 개를 추출한다.")
    @Test
    void extractOneSameSlackId() {
        String text = "봄을 여러번 멘션 <@U1111111111> 한 메시지 내용 <@U1111111111> 봄을 총 세번 멘션 <@U1111111111>";
        Set<String> slackIds = slackIdExtractor.extract(text);

        assertAll(
                () -> assertThat(slackIds).hasSize(1),
                () -> assertThat(slackIds).contains("U1111111111")
        );
    }

    @DisplayName("다른 슬랙 아이디가 여러개 존재하는 경우 slackId의 개수만큼 추출한다.")
    @Test
    void extractSeveralSlackIds() {
        String text =
                "봄(<@U1111111111>), 써머(<@U2222222222>), 연로그(<@U3333333333>)를 한 번씩 멘션";
        Set<String> slackIds = slackIdExtractor.extract(text);

        assertAll(
                () -> assertThat(slackIds).hasSize(3),
                () -> assertThat(slackIds).containsAll(
                        List.of("U1111111111", "U2222222222", "U3333333333"))
        );
    }

    @DisplayName("중복 슬랙 아이디도 있고 다른 슬랙 아이디가 여러개 존재하는 경우 slackId의 개수만큼 추출한다.")
    @Test
    void extractSameAndSeveralSlackIds() {
        String text = "써머(<@U2222222222>), 연로그(<@U3333333333>)를 한 번씩 멘션"
                + "봄은 두 번 멘션 <@U1111111111> <@U1111111111>";
        Set<String> slackIds = slackIdExtractor.extract(text);

        assertAll(
                () -> assertThat(slackIds).hasSize(3),
                () -> assertThat(slackIds).containsAll(
                        List.of("U1111111111", "U2222222222", "U3333333333"))
        );
    }
}
