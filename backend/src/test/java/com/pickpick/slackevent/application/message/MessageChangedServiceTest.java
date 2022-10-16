package com.pickpick.slackevent.application.message;

import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.MemberFixture.SUMMER;
import static com.pickpick.fixture.MessageFixtures.PLAIN_20220712_14_00_00;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
import static com.pickpick.support.JsonUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.utils.TimeUtils;
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
class MessageChangedServiceTest {

    @Autowired
    private MessageChangedService messageChangedService;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private MemberRepository members;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private WorkspaceRepository workspaces;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("메시지 내용 및 수정 날짜 변경")
    @Test
    void changedMessage() {
        // given
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Member summer = members.save(SUMMER.create(jupjup));
        Channel notice = channels.save(NOTICE.create(jupjup));
        Message storedMessage = PLAIN_20220712_14_00_00.create(notice, summer);

        String updatedText = "Message is updated!";
        String modifiedDate = "1234567890.123456";

        // when
        String request = createMessageChangedEvent(storedMessage, updatedText, modifiedDate);
        messageChangedService.execute(request);

        // then
        Optional<Message> expected = messages.findBySlackId(storedMessage.getSlackId());

        assertAll(
                () -> assertThat(expected).isNotEmpty(),
                () -> assertThat(expected.get().getText()).isEqualTo(updatedText),
                () -> assertThat(expected.get().getModifiedDate()).isEqualTo(
                        TimeUtils.toLocalDateTime(modifiedDate))
        );
    }

    @DisplayName("subtype이 메시지 수정 이벤트 발생이지만, message 내부에 thread_broadcast 타입이 있다면 메시지 저장")
    @Test
    void saveThreadBroadcastMessage() {
        // given 
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Member summer = members.save(SUMMER.create(jupjup));
        Channel notice = channels.save(NOTICE.create(jupjup));
        Message message = PLAIN_20220712_14_00_00.create(notice, summer);

        Optional<Message> beforeSaveMessage = messages.findBySlackId(message.getSlackId());

        // when
        String request = messageThreadBroadcastEvent(message);
        messageChangedService.execute(request);

        // then
        Optional<Message> afterSaveMessage = messages.findBySlackId(message.getSlackId());

        assertAll(
                () -> assertThat(beforeSaveMessage).isEmpty(),
                () -> assertThat(afterSaveMessage).isPresent(),
                () -> assertThat(afterSaveMessage.get().getSlackId()).isEqualTo(message.getSlackId())
        );
    }

    private String createMessageChangedEvent(final Message message, final String updatedText,
                                             final String modifiedDate) {
        Map<String, Object> event = Map.of(
                "type", SlackEvent.MESSAGE_CHANGED.getType(),
                "subtype", SlackEvent.MESSAGE_CHANGED.getSubtype(),
                "channel", message.getChannel().getSlackId(),
                "message", Map.of(
                        "user", message.getMember().getSlackId(),
                        "ts", modifiedDate,
                        "text", updatedText,
                        "client_msg_id", message.getSlackId()),
                "user", message.getMember().getSlackId(),
                "ts", modifiedDate,
                "text", updatedText,
                "client_msg_id", message.getSlackId());

        Map<String, Object> request = Map.of("event", event);
        return toJson(request);
    }

    private String messageThreadBroadcastEvent(final Message message) {
        Map<String, Object> event = Map.of(
                "type", "message",
                "subtype", "message_changed",
                "channel", message.getChannel().getSlackId(),
                "message", Map.of(
                        "type", SlackEvent.MESSAGE_THREAD_BROADCAST.getType(),
                        "subtype", SlackEvent.MESSAGE_THREAD_BROADCAST.getSubtype(),
                        "user", message.getMember().getSlackId(),
                        "ts", "1234567890.123456",
                        "text", "스레드의 메시지를 채널로 전송 텍스트",
                        "client_msg_id", message.getSlackId()
                ),
                "user", message.getMember().getSlackId(),
                "ts", "1234567890.123456",
                "text", "스레드의 메시지를 채널로 전송 텍스트",
                "client_msg_id", message.getSlackId());

        Map<String, Object> request = Map.of("event", event);
        return toJson(request);
    }
}
