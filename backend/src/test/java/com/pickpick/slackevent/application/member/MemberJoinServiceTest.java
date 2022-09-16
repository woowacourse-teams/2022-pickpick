package com.pickpick.slackevent.application.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickpick.config.DatabaseCleaner;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.slackevent.application.SlackEvent;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("MemberJoinService는")
@SpringBootTest
class MemberJoinServiceTest {

    private static final String SLACK_ID = "U03MKN0UW";

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

    @DisplayName("MEMBER_JOIN 타입에 대해서만 true를 반환한다")
    @CsvSource(value = {"MEMBER_JOIN,true",
            "MESSAGE_CREATED,false", "MESSAGE_CHANGED,false", "MESSAGE_DELETED,false",
            "CHANNEL_RENAME,false", "CHANNEL_DELETED,false", "MEMBER_CHANGED,false"})
    @ParameterizedTest(name = "SlackEvent: {0} - Supports: {1}")
    void supportsMemberJoinEvent(final SlackEvent slackEvent, final boolean expected) {
        // given & when
        boolean isSameSlackEvent = memberJoinService.isSameSlackEvent(slackEvent);

        // then
        assertThat(isSameSlackEvent).isEqualTo(expected);
    }

    @DisplayName("신규 멤버를 저장한다")
    @CsvSource(value = {"김진짜, 표시 이름, 표시 이름", "김진짜, '', 김진짜"})
    @ParameterizedTest(name = "RealName: {0}, DisplayName: {1} -> ExpectedName: {2}")
    void teamJoinEvent(final String realName, final String displayName, final String expectedName) {
        // given
        Optional<Member> memberBeforeSave = members.findBySlackId(SLACK_ID);
        String teamJoinEvent = createTeamJoinEvent(realName, displayName, expectedName);

        // when
        memberJoinService.execute(teamJoinEvent);
        Optional<Member> memberAfterSave = members.findBySlackId(SLACK_ID);

        // then
        assertAll(
                () -> assertThat(memberBeforeSave).isNotPresent(),
                () -> assertThat(memberAfterSave).isPresent(),
                () -> assertThat(memberAfterSave.get().getUsername()).isEqualTo(expectedName)
        );
    }

    private String createTeamJoinEvent(final String realName, final String displayName,
                                       final String thumbnailUrl) {
        return toJson(
                Map.of(
                        "event", Map.of(
                                "type", "team_join",
                                "user", Map.of(
                                        "id", SLACK_ID,
                                        "profile", Map.of(
                                                "real_name", realName,
                                                "display_name", displayName,
                                                "image_48", thumbnailUrl
                                        )
                                )
                        ))
        );
    }

    private String toJson(Map<String, Object> map) {
        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
