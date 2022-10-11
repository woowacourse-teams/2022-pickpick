package com.pickpick.slackevent.application.channel;

import static com.pickpick.support.JsonUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.fixture.ChannelFixture;
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
class ChannelCreatedServiceTest {

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private WorkspaceRepository workspaces;

    @Autowired
    private ChannelCreatedService channelCreatedService;

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
        Workspace workspace = workspaces.save(new Workspace("T000111", "xoxb-1234"));
        Channel channel = ChannelFixture.QNA.create();
        String request = toJson(
                Map.of(
                        "team_id", workspace.getSlackId(),
                        "event", Map.of(
                                "type", "channel_Created",
                                "channel", Map.of(
                                        "id", channel.getSlackId(),
                                        "name", channel.getName(),
                                        "created", "1234567890")
                        )
                )
        );

        // when
        channelCreatedService.execute(request);

        // then
        Optional<Channel> actual = channels.findBySlackId(channel.getSlackId());
        assertThat(actual).isNotEmpty();
    }
}
