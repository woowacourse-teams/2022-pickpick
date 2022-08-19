package com.pickpick.slackevent.application.channel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.channel.ChannelNotFoundException;
import java.util.Map;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class ChannelRenameServiceTest {

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private ChannelRenameService channelRenameService;

    @DisplayName("채널 이름 변경 이벤트가 전달되면 채널 이름이 변경된다")
    @Test
    void channelNameShouldBeChangedOnChannelRenameEvent() {
        // given
        Channel channel = channels.save(new Channel("slackId", "channelName"));

        String expectedChannelName = "변경된 채널 이름";
        Map<String, Object> request = Map.of(
                "type", "channel_rename",
                "channel", Map.of(
                        "id", channel.getSlackId(),
                        "name", expectedChannelName,
                        "created", "1234567890")
        );

        // when
        channelRenameService.execute(request);

        // then
        Channel renamedChannel = channels.findById(channel.getId())
                .orElseThrow(() -> new ChannelNotFoundException(channel.getId()));

        assertThat(renamedChannel.getName()).isEqualTo(expectedChannelName);
    }

    @DisplayName("채널 이름 변경 이벤트가 전달되었지만 해당 채널이 존재하지 않으면 예외가 발생한다")
    @Test
    void exceptionOccursWhenMatchedChannelDoesNotExist() {
        // given
        Map<String, Object> request = Map.of(
                "type", "channel_rename",
                "channel", Map.of(
                        "id", "NOT_EXIST_CHANNEL_SLACK_ID",
                        "name", "NAME CHANGE REQUEST VALUE",
                        "created", "1234567890")
        );

        // when & then
        assertThatThrownBy(() -> channelRenameService.execute(request))
                .isInstanceOf(ChannelNotFoundException.class);
    }
}
