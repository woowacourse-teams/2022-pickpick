package com.pickpick.slackevent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.slackevent.application.member.MemberChangedService;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberChangedServiceTest {

    private static final String SLACK_ID = "U03MKN0UW";

    @Autowired
    private MemberChangedService memberChangedService;

    @Autowired
    private MemberRepository members;


    @DisplayName("사용자 이름 및 프로필 이미지 변경")
    @Test
    void changedMember() {
        // given
        Member member = new Member(SLACK_ID, "사용자", "test.png");
        members.save(member);

        String username = "new_name";
        String thumbnailUrl = "new_test.png";
        Map<String, Object> request = memberChangedEvent(username, thumbnailUrl);

        // when
        memberChangedService.execute(request);

        // then
        Optional<Member> expected = members.findById(member.getId());

        assertAll(
                () -> assertThat(expected).isNotEmpty(),
                () -> assertThat(expected.get().getUsername()).isEqualTo(username),
                () -> assertThat(expected.get().getThumbnailUrl()).isEqualTo(thumbnailUrl)
        );
    }

    private Map<String, Object> memberChangedEvent(String username, String thumbnailUrl) {
        return Map.of(
                "user", Map.of(
                        "id", SLACK_ID,
                        "profile", Map.of(
                                "display_name", username,
                                "image_512", thumbnailUrl
                        )
                ));
    }
}
