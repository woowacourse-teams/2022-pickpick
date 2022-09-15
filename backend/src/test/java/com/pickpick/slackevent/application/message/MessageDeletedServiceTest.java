package com.pickpick.slackevent.application.message;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.config.DatabaseCleaner;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageDeletedServiceTest {

    private static final String MESSAGE_SLACK_ID = "db8a1f84-8acf-46ab-b93d-85177cee3e97";
    private static final Member SAMPLE_MEMBER = new Member("U03MKN0UW", "사용자", "test.png");
    private static final Channel SAMPLE_CHANNEL = new Channel("ASDFB", "채널");
    private static final Message SAMPLE_MESSAGE = new Message(
            MESSAGE_SLACK_ID,
            "메시지 전송!",
            SAMPLE_MEMBER,
            SAMPLE_CHANNEL,
            LocalDateTime.now(),
            LocalDateTime.now()
    );

    @Autowired
    private MessageDeletedService messageDeletedService;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private MemberRepository members;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("메시지 slack id가 전달되었을 때 메시지를 삭제한다.")
    @Test
    void deletedMessage() {
        // given
        saveMessage();
        Map<String, String> previousMessage = Map.of("client_msg_id", MESSAGE_SLACK_ID);
        Map<String, Object> event = Map.of("previous_message", previousMessage);
        Map<String, Object> request = Map.of("event", event);

        // when
        messageDeletedService.execute(request);

        // then
        Optional<Message> findMessage = messages.findBySlackId(MESSAGE_SLACK_ID);
        assertThat(findMessage).isEmpty();
    }

    private void saveMessage() {
        members.saveAll(List.of(SAMPLE_MEMBER));

        channels.save(SAMPLE_CHANNEL);

        messages.save(SAMPLE_MESSAGE);
    }
}
