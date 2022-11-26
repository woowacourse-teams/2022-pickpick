package com.pickpick.slackevent.application.channel;

import static com.pickpick.fixture.ChannelFixture.QNA;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
import static com.pickpick.support.JsonUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChannelCreatedEventHandlerTest {

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private WorkspaceRepository workspaces;

    @Autowired
    private ChannelCreatedEventHandler channelCreatedService;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("채널 생성 이벤트가 전달되면 채널이 생성된다")
    @Test
    void channelCreate() {
        // given
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Channel channel = QNA.create(jupjup);
        String request = createChannelCreatedRequest(channel);

        // when
        channelCreatedService.execute(request);

        // then
        Optional<Channel> actual = channels.findBySlackId(channel.getSlackId());
        assertThat(actual).isNotEmpty();
    }

    private String createChannelCreatedRequest(final Channel channel) {
        return toJson(
                Map.of(
                        "team_id", channel.getWorkspace().getSlackId(),
                        "event", Map.of(
                                "type", SlackEvent.CHANNEL_CREATED.getType(),
                                "channel", Map.of(
                                        "id", channel.getSlackId(),
                                        "name", channel.getName(),
                                        "created", "1234567890")
                        )
                )
        );
    }
}
