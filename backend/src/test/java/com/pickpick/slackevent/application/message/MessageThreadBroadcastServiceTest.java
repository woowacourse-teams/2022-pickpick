package com.pickpick.slackevent.application.message;

import static com.pickpick.support.JsonUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.fixture.ChannelFixture;
import com.pickpick.fixture.MemberFixture;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.message.dto.SlackMessageDto;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.utils.TimeUtils;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageThreadBroadcastServiceTest {

    private final Member SAMPLE_MEMBER = MemberFixture.BOM.create();
    private final Channel SAMPLE_CHANNEL = ChannelFixture.NOTICE.create();
    private final Message SAMPLE_MESSAGE = new Message(
            "db8a1f84-8acf-46ab-b93d-85177cee3e96",
            "메시지 전송!",
            SAMPLE_MEMBER,
            SAMPLE_CHANNEL,
            TimeUtils.toLocalDateTime("1656919966.864259"),
            TimeUtils.toLocalDateTime("1656919966.864259")
    );
    private final String MESSAGE_THREAD_BROADCAST_REQUEST = toJson(
            Map.of("event", Map.of(
                            "type", SlackEvent.MESSAGE_THREAD_BROADCAST.getType(),
                            "subtype", SlackEvent.MESSAGE_THREAD_BROADCAST.getSubtype(),
                            "channel", SAMPLE_CHANNEL.getSlackId(),
                            "text", SAMPLE_MESSAGE.getText(),
                            "user", SAMPLE_MEMBER.getSlackId(),
                            "ts", "1656919966.864259",
                            "client_msg_id", SAMPLE_MESSAGE.getSlackId()
                    )
            )
    );
    @Autowired
    private MessageThreadBroadcastService messageThreadBroadcastService;

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

    @DisplayName("스레드 메시지 채널로 전송 이벤트 전달 시 메시지를 저장한다")
    @Test
    void saveMessageWhenMessageCreatedEventPassed() {
        // given
        members.save(SAMPLE_MEMBER);
        channels.save(SAMPLE_CHANNEL);
        Optional<Channel> channelBeforeSave = channels.findBySlackId(SAMPLE_CHANNEL.getSlackId());
        Optional<Message> messageBeforeSave = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

        // when
        messageThreadBroadcastService.execute(MESSAGE_THREAD_BROADCAST_REQUEST);
        Optional<Channel> channelAfterSave = channels.findBySlackId(SAMPLE_CHANNEL.getSlackId());
        Optional<Message> messageAfterSave = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

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
        members.save(SAMPLE_MEMBER);
        channels.save(SAMPLE_CHANNEL);
        messages.save(SAMPLE_MESSAGE);
        Optional<Message> messageBeforeExecute = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

        SlackMessageDto messageDto = new SlackMessageDto(
                SAMPLE_MEMBER.getSlackId(),
                SAMPLE_MESSAGE.getSlackId(),
                "1656919966.864259",
                "1656919966.864259",
                "수정된 메시지 텍스트",
                SAMPLE_CHANNEL.getSlackId());

        // when
        messageThreadBroadcastService.saveWhenSubtypeIsMessageChanged(messageDto);

        // then
        Optional<Message> messageAfterExecute = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

        assertAll(
                () -> assertThat(messageBeforeExecute.get().getText()).isNotEqualTo(
                        messageAfterExecute.get().getText()),
                () -> assertThat(messageAfterExecute.get().getText()).isEqualTo("수정된 메시지 텍스트")
        );
    }

    @DisplayName("스레드 메시지 채널로 전송 이벤트 전달 시 subtype이 message_changed인 요청이 왔을 경우, DB에 저장되지 않은 메시지라면 저장한다")
    @Test
    void save() {
        // given
        members.save(SAMPLE_MEMBER);
        channels.save(SAMPLE_CHANNEL);
        Optional<Message> messageBeforeExecute = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

        SlackMessageDto messageDto = new SlackMessageDto(
                SAMPLE_MEMBER.getSlackId(),
                SAMPLE_MESSAGE.getSlackId(),
                "1656919966.864259",
                "1656919966.864259",
                "messageText",
                SAMPLE_CHANNEL.getSlackId());

        // when
        messageThreadBroadcastService.saveWhenSubtypeIsMessageChanged(messageDto);

        // then
        Optional<Message> messageAfterExecute = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

        assertAll(
                () -> assertThat(messageBeforeExecute).isEmpty(),
                () -> assertThat(messageAfterExecute).isPresent(),
                () -> assertThat(messageAfterExecute.get().getText()).isEqualTo("messageText")
        );
    }
}
