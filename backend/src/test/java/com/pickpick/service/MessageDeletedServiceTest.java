package com.pickpick.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.entity.Member;
import com.pickpick.entity.Message;
import com.pickpick.repository.MemberRepository;
import com.pickpick.repository.MessageRepository;
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
class MessageDeletedServiceTest {

    private static final String MESSAGE_SLACK_ID = "db8a1f84-8acf-46ab-b93d-85177cee3e97";
    private static final Member SAMPLE_MEMBER = new Member("U03MKN0UW", "사용자", "test.png");
    private static final Message SAMPLE_MESSAGE = new Message(
            MESSAGE_SLACK_ID,
            "메시지 전송!",
            SAMPLE_MEMBER,
            LocalDateTime.now(),
            LocalDateTime.now()
    );

    @Autowired
    private MessageDeletedService messageDeletedService;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private MemberRepository members;

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
        Member member = SAMPLE_MEMBER;
        members.saveAll(List.of(member));

        Message message = SAMPLE_MESSAGE;
        messages.save(message);
    }
}
