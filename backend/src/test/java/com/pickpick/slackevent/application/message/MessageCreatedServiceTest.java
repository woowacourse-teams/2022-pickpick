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
class MessageCreatedServiceTest {

    private static final Member SAMPLE_MEMBER = MemberFixture.BOM.create();
    private static final Channel SAMPLE_CHANNEL = ChannelFixture.NOTICE.create();
    private static final Message SAMPLE_MESSAGE = new Message(
            "db8a1f84-8acf-46ab-b93d-85177cee3e97",
            "메시지 전송!",
            SAMPLE_MEMBER,
            SAMPLE_CHANNEL,
            TimeUtils.toLocalDateTime("1656919966.864259"),
            TimeUtils.toLocalDateTime("1656919966.864259")
    );
    private static final String MESSAGE_CREATED_REQUEST = toJson(
            Map.of("event", Map.of(
                    "type", "message",
                    "channel", SAMPLE_CHANNEL.getSlackId(),
                    "text", SAMPLE_MESSAGE.getText(),
                    "user", SAMPLE_MEMBER.getSlackId(),
                    "ts", "1656919966.864259",
                    "client_msg_id", SAMPLE_MESSAGE.getSlackId())
            )
    );
    private static final String MESSAGE_REPLIED_REQUEST = toJson(
            Map.of("event", Map.of(
                    "type", "message",
                    "channel", SAMPLE_CHANNEL.getSlackId(),
                    "text", SAMPLE_MESSAGE.getText(),
                    "user", SAMPLE_MEMBER.getSlackId(),
                    "ts", "1656919966.864259",
                    "client_msg_id", SAMPLE_MESSAGE.getSlackId(),
                    "thread_ts", "1234599999")
            )
    );

    @Autowired
    private MessageCreatedService messageCreatedService;

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

    @DisplayName("메시지 작성 이벤트 전달 시 메시지를 저장한다")
    @Test
    void saveMessageWhenMessageCreatedEventPassed() {
        // given
        members.save(SAMPLE_MEMBER);
        channels.save(SAMPLE_CHANNEL);
        Optional<Channel> channelBeforeSave = channels.findBySlackId(SAMPLE_CHANNEL.getSlackId());
        Optional<Message> messageBeforeSave = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

        // when
        messageCreatedService.execute(MESSAGE_CREATED_REQUEST);
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

    @DisplayName("메시지 댓글 생성 이벤트는 전달되어도 내용을 저장하지 않는다")
    @Test
    void doNotSaveReplyMessage() {
        //given
        members.save(SAMPLE_MEMBER);
        channels.save(SAMPLE_CHANNEL);

        // when
        messageCreatedService.execute(MESSAGE_REPLIED_REQUEST);

        // then
        Optional<Message> message = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

        assertThat(message).isEmpty();
    }
}
