package com.pickpick.slackevent.application.message;

import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.MemberFixture.BOM;
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
import com.pickpick.slackevent.application.message.dto.SlackMessageDto;
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
class MessageThreadBroadcastServiceTest {

    @Autowired
    private MessageThreadBroadcastService messageThreadBroadcastService;

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

    @DisplayName("스레드 메시지 채널로 전송 이벤트 전달 시 메시지를 저장한다")
    @Test
    void saveMessageWhenMessageCreatedEventPassed() {
        // given
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Member bom = members.save(BOM.create(jupjup));
        Channel notice = channels.save(NOTICE.create(jupjup));
        Message message = PLAIN_20220712_14_00_00.create(notice, bom);

        Optional<Channel> channelBeforeSave = channels.findBySlackId(notice.getSlackId());
        Optional<Message> messageBeforeSave = messages.findBySlackId(message.getSlackId());

        // when
        String request = createMessageThreadBroadcastRequest(notice, message, bom);
        messageThreadBroadcastService.execute(request);

        Optional<Channel> channelAfterSave = channels.findBySlackId(notice.getSlackId());
        Optional<Message> messageAfterSave = messages.findBySlackId(message.getSlackId());

        // then
        assertAll(
                () -> assertThat(channelBeforeSave).isPresent(),
                () -> assertThat(messageBeforeSave).isEmpty(),
                () -> assertThat(channelAfterSave).isPresent(),
                () -> assertThat(messageAfterSave).isPresent()
        );
    }

    @DisplayName("스레드 메시지 채널로 전송 이벤트 전달 시 subtype이 message_changed인 요청이 왔을 경우, DB에 저장되어 있는 메시지라면 수정한다.")
    @Test
    void notSave() {
        // given
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Member bom = members.save(BOM.create(jupjup));
        Channel notice = channels.save(NOTICE.create(jupjup));
        Message message = messages.save(PLAIN_20220712_14_00_00.create(notice, bom));

        Optional<Message> messageBeforeExecute = messages.findBySlackId(message.getSlackId());

        SlackMessageDto messageDto = new SlackMessageDto(
                bom.getSlackId(),
                message.getSlackId(),
                "1656919966.864259",
                "1656919966.864259",
                "수정된 메시지 텍스트",
                notice.getSlackId());

        // when
        messageThreadBroadcastService.saveWhenSubtypeIsMessageChanged(messageDto);

        // then
        Optional<Message> messageAfterExecute = messages.findBySlackId(message.getSlackId());

        assertAll(
                () -> assertThat(messageBeforeExecute).isPresent(),
                () -> assertThat(messageBeforeExecute.get().getText()).isNotEqualTo(
                        messageAfterExecute.get().getText()),
                () -> assertThat(messageAfterExecute.get().getText()).isEqualTo("수정된 메시지 텍스트")
        );
    }

    @DisplayName("스레드 메시지 채널로 전송 이벤트 전달 시 subtype이 message_changed인 요청이 왔을 경우, DB에 저장되지 않은 메시지라면 저장한다")
    @Test
    void save() {
        // given
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Member bom = members.save(BOM.create(jupjup));
        Channel notice = channels.save(NOTICE.create(jupjup));
        Message message = PLAIN_20220712_14_00_00.create(notice, bom);

        Optional<Message> messageBeforeExecute = messages.findBySlackId(message.getSlackId());

        SlackMessageDto messageDto = new SlackMessageDto(
                bom.getSlackId(),
                message.getSlackId(),
                "1656919966.864259",
                "1656919966.864259",
                "messageText",
                notice.getSlackId());

        // when
        messageThreadBroadcastService.saveWhenSubtypeIsMessageChanged(messageDto);

        // then
        Optional<Message> messageAfterExecute = messages.findBySlackId(message.getSlackId());

        assertAll(
                () -> assertThat(messageBeforeExecute).isEmpty(),
                () -> assertThat(messageAfterExecute).isPresent(),
                () -> assertThat(messageAfterExecute.get().getText()).isEqualTo("messageText")
        );
    }

    private String createMessageThreadBroadcastRequest(final Channel channel, final Message message,
                                                       final Member member) {
        return toJson(
                Map.of("event", Map.of(
                                "type", SlackEvent.MESSAGE_THREAD_BROADCAST.getType(),
                                "subtype", SlackEvent.MESSAGE_THREAD_BROADCAST.getSubtype(),
                                "channel", channel.getSlackId(),
                                "text", message.getText(),
                                "user", member.getSlackId(),
                                "ts", "1656919966.864259",
                                "client_msg_id", message.getSlackId()
                        )
                )
        );
    }
}
