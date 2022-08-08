package com.pickpick.slackevent.application.channel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import java.time.LocalDateTime;
import java.util.Map;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@Transactional
@SpringBootTest
class ChannelDeletedServiceTest {

    private static final Member SAMPLE_MEMBER = new Member("U03MKN0UW", "사용자", "test.png");
    private static final Channel SAMPLE_CHANNEL = new Channel("ASDFB", "채널");
    private static final Message SAMPLE_MESSAGE_1 = new Message(
            "aaaa1f84-8acf-46ab-b93d-85177cee3e97",
            "첫번째 메시지 전송!",
            SAMPLE_MEMBER,
            SAMPLE_CHANNEL,
            LocalDateTime.now(),
            LocalDateTime.now()
    );

    private static final Message SAMPLE_MESSAGE_2 = new Message(
            "bbbb1f84-8acf-46ab-b93d-85177cee3e99",
            "두번째 메시지 전송!",
            SAMPLE_MEMBER,
            SAMPLE_CHANNEL,
            LocalDateTime.now(),
            LocalDateTime.now()
    );

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private MemberRepository members;

    @Autowired
    private ChannelDeletedService channelDeletedService;

    @DisplayName("채널 삭제 이벤트가 전달되면 채널과 메시지들이 삭제된다")
    @Test
    void channelAndMessagesShouldBeDeletedOnChannelDeletedEvent() {
        // given
        channels.save(SAMPLE_CHANNEL);
        members.save(SAMPLE_MEMBER);
        messages.save(SAMPLE_MESSAGE_1);
        messages.save(SAMPLE_MESSAGE_2);

        Map<String, Object> request = Map.of(
                "type", "channel_deleted",
                "channel", SAMPLE_CHANNEL.getSlackId()
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
