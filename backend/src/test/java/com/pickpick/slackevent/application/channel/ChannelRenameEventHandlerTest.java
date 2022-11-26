package com.pickpick.slackevent.application.channel;

import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
import static com.pickpick.support.JsonUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.channel.ChannelNotFoundException;
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
class ChannelRenameEventHandlerTest {

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private WorkspaceRepository workspaces;

    @Autowired
    private ChannelRenameEventHandler channelRenameService;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("채널 이름 변경 이벤트가 전달되면 채널 이름이 변경된다")
    @Test
    void channelNameShouldBeChangedOnChannelRenameEvent() {
        // given
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Channel notice = channels.save(NOTICE.create(jupjup));
        String newChannelName = "변경된 채널 이름";

        String request = createChannelRenamedRequest(notice, newChannelName);

        // when
        channelRenameService.execute(request);

        // then
        Channel renamedChannel = channels.getById(notice.getId());
        assertThat(renamedChannel.getName()).isEqualTo(newChannelName);
    }

    private String createChannelRenamedRequest(final Channel channel, final String newChannelName) {
        return toJson(
                Map.of("event",
                        Map.of(
                                "type", SlackEvent.CHANNEL_RENAME.getType(),
                                "channel", Map.of(
                                        "id", channel.getSlackId(),
                                        "name", newChannelName,
                                        "created", "1234567890")
                        )
                )
        );
    }

    @DisplayName("채널 이름 변경 이벤트가 전달되었지만 해당 채널이 존재하지 않으면 예외가 발생한다")
    @Test
    void exceptionOccursWhenMatchedChannelDoesNotExist() {
        // given
        String request = toJson(
                Map.of("event",
                        Map.of(
                                "type", "channel_rename",
                                "channel", Map.of(
                                        "id", "NOT_EXIST_CHANNEL_SLACK_ID",
                                        "name", "NAME CHANGE REQUEST VALUE",
                                        "created", "1234567890")
                        )
                )
        );

        // when & then
        assertThatThrownBy(() -> channelRenameService.execute(request))
                .isInstanceOf(ChannelNotFoundException.class);
    }
}
