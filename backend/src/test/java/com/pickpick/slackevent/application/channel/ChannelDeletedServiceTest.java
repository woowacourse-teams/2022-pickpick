package com.pickpick.slackevent.application.channel;

import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.MemberFixture.HOPE;
import static com.pickpick.fixture.MessageFixtures.PLAIN_20220712_18_00_00;
import static com.pickpick.fixture.MessageFixtures.PLAIN_20220715_14_00_00;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
import static com.pickpick.support.JsonUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.slackevent.application.SlackEvent;
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
class ChannelDeletedServiceTest {

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private MemberRepository members;

    @Autowired
    private WorkspaceRepository workspaces;

    @Autowired
    private ChannelDeletedService channelDeletedService;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("채널 삭제 이벤트가 전달되면 채널과 메시지들이 삭제된다")
    @Test
    void channelAndMessagesShouldBeDeletedOnChannelDeletedEvent() {
        // given
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Member hope = members.save(HOPE.create(jupjup));
        Channel notice = channels.save(NOTICE.create(jupjup));
        messages.save(PLAIN_20220712_18_00_00.create(notice, hope));
        messages.save(PLAIN_20220715_14_00_00.create(notice, hope));

        String request = createChannelDeletedRequest(notice);

        // when
        channelDeletedService.execute(request);

        //then
        assertAll(
                () -> assertThat(channels.findAll()).isEmpty(),
                () -> assertThat(messages.findAll()).isEmpty()
        );
    }

    private String createChannelDeletedRequest(final Channel notice) {
        return toJson(
                Map.of("event",
                        Map.of(
                                "type", SlackEvent.CHANNEL_DELETED.getType(),
                                "channel", notice.getSlackId()
                        )
                )
        );
    }
}
