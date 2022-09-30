package com.pickpick.slackevent.application.member;

import static com.pickpick.support.JsonUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.exception.member.MemberNotFoundException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.support.DatabaseCleaner;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberJoinServiceTest {

    @Autowired
    private MemberJoinService memberJoinService;

    @Autowired
    private MemberRepository members;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("MEMBER_JOIN 이벤트 타입에 true 반환")
    @Test
    void supportsMemberJoinEventTrue() {
        // given & when
        boolean actual = memberJoinService.isSameSlackEvent(SlackEvent.MEMBER_JOIN);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("MEMBER_JOIN을 제외한 모든 이벤트 타입에 false 반환")
    @ValueSource(strings = {"MESSAGE_CREATED", "MESSAGE_CHANGED", "MESSAGE_DELETED",
            "CHANNEL_RENAME", "CHANNEL_DELETED", "MEMBER_CHANGED"})
    @ParameterizedTest
    void supportsOtherEventFalse(final SlackEvent slackEvent) {
        // given & when
        boolean actual = memberJoinService.isSameSlackEvent(slackEvent);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("MEMBER_JOIN 이벤트가 들어오면 신규 멤버를 저장")
    @Test
    void saveNewMember() {
        // given
        String slackId = "U0000000005";
        Optional<Member> memberBeforeJoin = members.findBySlackId(slackId);

        // when
        String teamJoinEvent = createTeamJoinEvent(slackId, "고재증", "꼬재");
        memberJoinService.execute(teamJoinEvent);

        // then
        Optional<Member> memberAfterJoin = members.findBySlackId(slackId);

        assertAll(
                () -> assertThat(memberBeforeJoin).isEmpty(),
                () -> assertThat(memberAfterJoin).isPresent()
        );
    }

    @DisplayName("신규 멤버의 display_name이 빈 문자열이 아니면 username으로 저장")
    @Test
    void saveUsernameAsDisplayName() {
        // given
        String slackId = "U0000000005";
        String realName = "고재증";
        String displayName = "꼬재";

        // when
        String teamJoinEvent = createTeamJoinEvent(slackId, realName, displayName);
        memberJoinService.execute(teamJoinEvent);

        // then
        Member memberAfterJoin = members.findBySlackId(slackId)
                .orElseThrow(() -> new MemberNotFoundException(slackId));

        assertThat(memberAfterJoin.getUsername()).isEqualTo(displayName);
    }

    @DisplayName("신규 멤버의 display_name이 빈 문자열이면 real_name을 username으로 저장")
    @Test
    void saveUsernameAsRealNameIfDisplayNameIsEmpty() {
        // given
        String slackId = "U0000000005";
        String realName = "고재증";
        String displayName = "";

        // when
        String teamJoinEvent = createTeamJoinEvent(slackId, realName, displayName);
        memberJoinService.execute(teamJoinEvent);

        // then
        Member memberAfterJoin = members.findBySlackId(slackId)
                .orElseThrow(() -> new MemberNotFoundException(slackId));

        assertThat(memberAfterJoin.getUsername()).isEqualTo(realName);
    }

    private String createTeamJoinEvent(final String slackId, final String realName, final String displayName) {
        return toJson(
                Map.of(
                        "event", Map.of(
                                "type", "team_join",
                                "user", Map.of(
                                        "id", slackId,
                                        "profile", Map.of(
                                                "real_name", realName,
                                                "display_name", displayName,
                                                "image_48", "https://kkojae.png"
                                        )
                                )
                        ))
        );
    }
}
