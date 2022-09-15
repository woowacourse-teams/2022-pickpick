package com.pickpick.slackevent.application.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickpick.config.DatabaseCleaner;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberChangedServiceTest {

    private static final String SLACK_ID = "U03MKN0UW";

    @Autowired
    private MemberChangedService memberChangedService;

    @Autowired
    private MemberRepository members;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("사용자 이름 변경")
    @CsvSource(value = {"김진짜, 표시 이름, 표시 이름", "김진짜, '', 김진짜"})
    @ParameterizedTest(name = "{1}이 들어오는 경우 {2}")
    void changedUsername(final String realName, final String displayName, final String expectedName) {
        // given
        Member member = members.save(new Member(SLACK_ID, "사용자", "test.png"));

        String request = memberChangedEvent(realName, displayName, "test.png");

        // when
        memberChangedService.execute(request);

        // then
        Optional<Member> actual = members.findById(member.getId());

        assertAll(
                () -> assertThat(actual).isNotEmpty(),
                () -> assertThat(actual.get().getUsername()).isEqualTo(expectedName)
        );
    }

    @DisplayName("사용자 프로필 이미지 변경")
    @Test
    void changedThumbnailUrl() {
        // given
        Member member = members.save(new Member(SLACK_ID, "사용자", "test.png"));

        String thumbnailUrl = "new_test.png";
        String request = memberChangedEvent("사용자", "표시 이름", thumbnailUrl);

        // when
        memberChangedService.execute(request);

        // then
        Optional<Member> actual = members.findById(member.getId());

        assertAll(
                () -> assertThat(actual).isNotEmpty(),
                () -> assertThat(actual.get().getThumbnailUrl()).isEqualTo(thumbnailUrl)
        );
    }

    private String memberChangedEvent(final String realName, final String displayName,
                                      final String thumbnailUrl) {
        Map<String, Object> request = Map.of("event", Map.of(
                "user", Map.of(
                        "id", SLACK_ID,
                        "profile", Map.of(
                                "real_name", realName,
                                "display_name", displayName,
                                "image_512", thumbnailUrl
                        )
                )
        ));

        try {
            return new ObjectMapper().writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
