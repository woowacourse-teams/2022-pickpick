package com.pickpick.slackevent.application.channel;

import static com.pickpick.support.JsonUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.fixture.MessageFixtures;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.support.DatabaseCleaner;
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
        Member hope = members.save(new Member("U00004", "호프", "https://hope.png"));
        Channel notice = channels.save(new Channel("C00001", "공지사항"));
        messages.save(MessageFixtures.PLAIN_20220712_18_00_00.create(notice, hope));
        messages.save(MessageFixtures.PLAIN_20220715_14_00_00.create(notice, hope));

        String request = toJson(
                Map.of("event",
                        Map.of(
                                "type", "channel_deleted",
                                "channel", notice.getSlackId()
                        )
                )
        );

        // when
        channelDeletedService.execute(request);

        //then
        assertAll(
                () -> assertThat(channels.findAll()).isEmpty(),
                () -> assertThat(messages.findAll()).isEmpty()
        );
    }
}
