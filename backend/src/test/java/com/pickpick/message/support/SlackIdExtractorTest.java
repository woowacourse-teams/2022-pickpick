package com.pickpick.message.support;

import static com.pickpick.fixture.MemberFixture.BOM;
import static com.pickpick.fixture.MemberFixture.SUMMER;
import static com.pickpick.fixture.MemberFixture.YEONLOG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.fixture.MemberFixture;
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
        String text = "봄을 멘션" + slackId(BOM) + "한 메시지 내용";
        Set<String> slackIds = slackIdExtractor.extract(text);

        assertAll(
                () -> assertThat(slackIds).hasSize(1),
                () -> assertThat(slackIds).contains(BOM.getSlackId())
        );
    }

    @DisplayName("같은 SlackId가 여러 개 존재하는 경우 SlackId 한 개를 추출한다.")
    @Test
    void extractOneSameSlackId() {
        String text = "봄을 여러번 멘션" + slackId(BOM) + "한 메시지 내용" + slackId(BOM) + "봄을 총 세번 멘션" + slackId(BOM);
        Set<String> slackIds = slackIdExtractor.extract(text);

        assertAll(
                () -> assertThat(slackIds).hasSize(1),
                () -> assertThat(slackIds).contains(BOM.getSlackId())
        );
    }

    @DisplayName("다른 슬랙 아이디가 여러개 존재하는 경우 slackId의 개수만큼 추출한다.")
    @Test
    void extractSeveralSlackIds() {
        String text = "봄, 써머, 연로그를 한 번씩 멘션" + slackId(BOM) + slackId(SUMMER) + slackId(YEONLOG);
        Set<String> slackIds = slackIdExtractor.extract(text);

        assertAll(
                () -> assertThat(slackIds).hasSize(3),
                () -> assertThat(slackIds).containsAll(
                        List.of(BOM.getSlackId(), SUMMER.getSlackId(), YEONLOG.getSlackId()))
        );
    }

    @DisplayName("중복 슬랙 아이디도 있고 다른 슬랙 아이디가 여러개 존재하는 경우 slackId의 개수만큼 추출한다.")
    @Test
    void extractSameAndSeveralSlackIds() {
        String text = "써머, 연로그를 한 번씩 멘션" + slackId(SUMMER) + slackId(YEONLOG)
                + "봄은 두 번 멘션" + slackId(BOM) + slackId(BOM);
        Set<String> slackIds = slackIdExtractor.extract(text);

        assertAll(
                () -> assertThat(slackIds).hasSize(3),
                () -> assertThat(slackIds).containsAll(
                        List.of(BOM.getSlackId(), SUMMER.getSlackId(), YEONLOG.getSlackId()))
        );
    }

    private String slackId(final MemberFixture fixture) {
        return MENTION_PREFIX + fixture.getSlackId() + MENTION_SUFFIX;
    }
}
