package com.pickpick.slackevent.application.member;

import static com.pickpick.fixture.MemberFixture.SUMMER;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
import static com.pickpick.support.JsonUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberChangedServiceTest {

    @Autowired
    private MemberChangedService memberChangedService;

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

    @DisplayName("사용자 이름 변경 시 display_name이 빈 문자열이 아니면 해당 값으로 변경")
    @Test
    void changeUsernameByDisplayName() {
        // given
        Workspace workspace = workspaces.save(JUPJUP.create());
        Member summer = members.save(SUMMER.create(workspace));

        // when
        String realName = "최혜원";
        String displayName = "겨울";

        String request = memberChangedEvent(summer.getSlackId(), realName, displayName, summer.getThumbnailUrl());
        memberChangedService.execute(request);

        // then
        Member actual = members.getById(summer.getId());

        assertThat(actual.getUsername()).isEqualTo(displayName);
    }

    @DisplayName("사용자 이름 변경 시 display_name 이 빈 문자열이라면 real_name 으로 변경")
    @Test
    void changeUsernameByRealNameWhenDisplayNameIsBlank() {
        // given
        Workspace workspace = workspaces.save(JUPJUP.create());
        Member summer = members.save(SUMMER.create(workspace));

        // when
        String realName = "최혜원";
        String displayName = "";

        String request = memberChangedEvent(summer.getSlackId(), realName, displayName, summer.getThumbnailUrl());
        memberChangedService.execute(request);

        // then
        Member actual = members.getById(summer.getId());

        assertThat(actual.getUsername()).isEqualTo(realName);
    }

    @DisplayName("사용자 프로필 이미지 변경")
    @Test
    void changedThumbnailUrl() {
        // given
        Workspace workspace = workspaces.save(JUPJUP.create());
        Member summer = members.save(SUMMER.create(workspace));

        // when
        String changedThumbnailUrl = "https://hyewon.png";
        String request = memberChangedEvent(summer.getSlackId(), "최혜원", summer.getUsername(), changedThumbnailUrl);
        memberChangedService.execute(request);

        // then
        Member actual = members.getById(summer.getId());

        assertThat(actual.getThumbnailUrl()).isEqualTo(changedThumbnailUrl);
    }

    private String memberChangedEvent(final String slackId, final String realName,
                                      final String displayName, final String thumbnailUrl) {
        Map<String, Object> request = Map.of("event", Map.of(
                "user", Map.of(
                        "id", slackId,
                        "profile", Map.of(
                                "real_name", realName,
                                "display_name", displayName,
                                "image_48", thumbnailUrl
                        )
                )
        ));

        return toJson(request);
    }
}
