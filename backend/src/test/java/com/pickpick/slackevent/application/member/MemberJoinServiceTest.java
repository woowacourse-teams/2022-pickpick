package com.pickpick.slackevent.application.member;

import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
import static com.pickpick.support.JsonUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
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
    private WorkspaceRepository workspaces;

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
        Workspace jupjup = workspaces.save(JUPJUP.create());
        String slackId = "U0000000001";
        Optional<Member> memberBeforeJoin = members.findBySlackId(slackId);

        // when
        String request = createTeamJoinEvent(slackId, jupjup.getSlackId(), "최혜원", "써머");
        memberJoinService.execute(request);

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
        Workspace jupjup = workspaces.save(JUPJUP.create());
        String slackId = "U0000000001";
        String realName = "최혜원";
        String displayName = "써머";

        // when
        String request = createTeamJoinEvent(slackId, jupjup.getSlackId(), realName, displayName);
        memberJoinService.execute(request);

        // then
        Member memberAfterJoin = members.getBySlackId(slackId);

        assertThat(memberAfterJoin.getUsername()).isEqualTo(displayName);
    }

    @DisplayName("신규 멤버의 display_name이 빈 문자열이면 real_name을 username으로 저장")
    @Test
    void saveUsernameAsRealNameIfDisplayNameIsEmpty() {
        // given
        Workspace jupjup = workspaces.save(JUPJUP.create());
        String slackId = "U0000000001";
        String realName = "최혜원";
        String displayName = "";

        // when
        String request = createTeamJoinEvent(slackId, jupjup.getSlackId(), realName, displayName);
        memberJoinService.execute(request);

        // then
        Member memberAfterJoin = members.getBySlackId(slackId);

        assertThat(memberAfterJoin.getUsername()).isEqualTo(realName);
    }

    private String createTeamJoinEvent(final String slackId, final String workspaceSlackId, final String realName,
                                       final String displayName) {
        return toJson(
                Map.of(
                        "team_id", workspaceSlackId,
                        "event", Map.of(
                                "type", "team_join",
                                "user", Map.of(
                                        "id", slackId,
                                        "profile", Map.of(
                                                "real_name", realName,
                                                "display_name", displayName,
                                                "image_48", "https://summer.png"
                                        )
                                )
                        ))
        );
    }
}
