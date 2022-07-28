package com.pickpick.slackevent.application.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.utils.TimeUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MessageChangedServiceTest {

    private static final Member SAMPLE_MEMBER = new Member("U03MKN0UW", "사용자", "test.png");
    private static final Channel SAMPLE_CHANNEL = new Channel("ASDFB", "채널");
    private static final Message SAMPLE_MESSAGE = new Message(
            "db8a1f84-8acf-46ab-b93d-85177cee3e97",
            "메시지 전송!",
            SAMPLE_MEMBER,
            SAMPLE_CHANNEL,
            LocalDateTime.now(),
            LocalDateTime.now()
    );

    @Autowired
    private MessageChangedService messageChangedService;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private MemberRepository members;

    @Autowired
    private ChannelRepository channels;

    @DisplayName("메시지 내용 및 수정 날짜 변경")
    @Test
    void changedMessage() {
        // given
        saveMessage();
        String updatedText = "Message is updated!";
        String modifiedDate = "1234567890.123456";
        Map<String, Object> request = messageChangedEvent(updatedText, modifiedDate);

        // when
        messageChangedService.execute(request);

        // then
        Optional<Message> expected = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

        assertAll(
                () -> assertThat(expected).isNotEmpty(),
                () -> assertThat(expected.get().getText()).isEqualTo(updatedText),
                () -> assertThat(expected.get().getModifiedDate()).isEqualTo(
                        TimeUtils.toLocalDateTime(modifiedDate))
        );
    }

    private void saveMessage() {
        members.saveAll(List.of(SAMPLE_MEMBER));

        channels.save(SAMPLE_CHANNEL);

        messages.save(SAMPLE_MESSAGE);
    }

    private Map<String, Object> messageChangedEvent(String updatedText, String modifiedDate) {
        Map<String, Object> event = Map.of(
                "type", "message",
                "subtype", "message_changed",
                "channel", SAMPLE_CHANNEL.getSlackId(),
                "message", Map.of(
                        "user", SAMPLE_MEMBER.getSlackId(),
                        "ts", modifiedDate,
                        "text", updatedText,
                        "client_msg_id", SAMPLE_MESSAGE.getSlackId()),
                "user", SAMPLE_MEMBER.getSlackId(),
                "ts", modifiedDate,
                "text", updatedText,
                "client_msg_id", SAMPLE_MESSAGE.getSlackId());

        Map<String, Object> request = Map.of("event", event);
        return request;
    }
}
