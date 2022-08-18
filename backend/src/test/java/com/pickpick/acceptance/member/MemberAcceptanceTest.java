package com.pickpick.acceptance.member;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("멤버 기능")
@SuppressWarnings("NonAsciiCharacters")
public class MemberAcceptanceTest extends AcceptanceTest {

    private static final String SLACK_EVENT_API_URL = "/api/event";
    private static final String SLACK_ID = "U03MKN0UW";

    @Autowired
    private MemberRepository members;

    @Disabled
    @Test
    void 프로젝트_기동_시점에_유저가_저장되어_있어야_한다() {
        // given
        List<Member> members = this.members.findAll();

        // when & then
        assertThat(members.isEmpty()).isFalse();
    }

    @Test
    void 슬랙_워크스페이스에_신규_멤버가_참여하면_저장되어야_한다() {
        // given
        int 신규_참여_전_멤버_수 = members.findAll().size();
        Map<String, Object> teamJoinEvent = createTeamJoinEvent("진짜이름", "표시이름", "https://somebody.png");

        // when
        ExtractableResponse<Response> teamJoinEventResponse = post(SLACK_EVENT_API_URL, teamJoinEvent);
        List<Member> 신규_참여_후_전체_멤버 = members.findAll();
        int 신규_참여_후_멤버_수 = 신규_참여_후_전체_멤버.size();
        Optional<Member> 신규_참여_멤버 = 신규_참여_후_전체_멤버.stream()
                .max(Comparator.comparing(Member::getId));

        // then
        assertAll(
                () -> 상태코드_200_확인(teamJoinEventResponse),
                () -> assertThat(신규_참여_전_멤버_수 + 1).isEqualTo(신규_참여_후_멤버_수),
                () -> assertThat(신규_참여_멤버).isPresent(),
                () -> assertThat(신규_참여_멤버.get().getSlackId()).isEqualTo(SLACK_ID)
        );
    }

    private Map<String, Object> createTeamJoinEvent(final String realName, final String displayName,
                                                    final String thumbnailUrl) {
        return Map.of(
                "event", Map.of(
                        "type", "team_join",
                        "user", Map.of(
                                "id", SLACK_ID,
                                "profile", Map.of(
                                        "real_name", realName,
                                        "display_name", displayName,
                                        "image_512", thumbnailUrl
                                )
                        )
                ));
    }
}
