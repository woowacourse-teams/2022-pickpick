package com.pickpick.workspace.application;

import static com.pickpick.fixture.MemberFixture.KKOJAE;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.workspace.WorkspaceDuplicateException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WorkspaceServiceTest {

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private MemberRepository members;

    @Autowired
    private WorkspaceRepository workspaces;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("워크스페이스 초기화 시 워크스페이스를 저장한다.")
    @Test
    void registerWorkspace() {
        // given
        Workspace jupjup = JUPJUP.create();

        Optional<Workspace> beforeSave = workspaces.findBySlackId(jupjup.getSlackId());

        // when
        workspaceService.register(KKOJAE.getCode());

        Optional<Workspace> afterSave = workspaces.findBySlackId(jupjup.getSlackId());

        // then
        assertThat(beforeSave).isEmpty();
        assertThat(afterSave).isNotEmpty();
    }

    @DisplayName("워크스페이스 초기화 시 해당 워크스페이스의 채널과 멤버를 저장한다.")
    @Test
    void registerWorkspaceAnd() {
        // given
        Workspace jupjup = JUPJUP.create();
        Member kkojae = KKOJAE.createNeverLoggedIn(jupjup);

        // when
        workspaceService.register(KKOJAE.getCode());

        Workspace savedJupjup = workspaces.getBySlackId(jupjup.getSlackId());
        List<Channel> jupjupChannels = channels.findAllByWorkspaceOrderByName(savedJupjup);
        Member savedKkojae = members.getBySlackId(kkojae.getSlackId());

        List<Member> memberInJupjup = members.findAllByWorkspace(savedJupjup);

        // then
        assertThat(jupjupChannels).isNotEmpty();
        assertThat(memberInJupjup).extracting("slackId").contains(savedKkojae.getSlackId());
    }

    @DisplayName("이미 등록된 워크스페이스를 재등록시 예외가 발생한다.")
    @Test
    void registerExistedWorkspace() {
        // given
        Workspace jupjup = JUPJUP.create();

        // when
        workspaceService.register(KKOJAE.getCode());

        // then
        assertThatThrownBy(() -> workspaceService.register(KKOJAE.getCode()))
                .isInstanceOf(WorkspaceDuplicateException.class);
    }
}
