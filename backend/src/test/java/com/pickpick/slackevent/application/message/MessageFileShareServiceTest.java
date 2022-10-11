package com.pickpick.slackevent.application.message;

import static com.pickpick.slackevent.application.SlackEvent.MESSAGE_FILE_SHARE;
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
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageFileShareServiceTest {

    private static final Member SAMPLE_MEMBER = MemberFixture.BOM.create();
    private static final Channel SAMPLE_CHANNEL = ChannelFixture.QNA.create();
    private static final Message SAMPLE_MESSAGE = new Message(
            "db8a1f84-8acf-46ab-b93d-85177cee3e97",
            "메시지 전송!",
            SAMPLE_MEMBER,
            SAMPLE_CHANNEL,
            TimeUtils.toLocalDateTime("1656919966.864259"),
            TimeUtils.toLocalDateTime("1656919966.864259")
    );

    @Autowired
    private MessageFileShareService messageFileShareService;

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

    @DisplayName("파일 공유 이벤트 전달 시 채널이 저장되어 있으면 메시지를 저장한다")
    @ValueSource(strings = {"", " ", "파일과 함께 전송한 메시지 text"})
    @ParameterizedTest
    void saveMessageWhenFileShareEventPassed(final String expectedText) {
        // given
        members.save(SAMPLE_MEMBER);
        channels.save(SAMPLE_CHANNEL);
        Optional<Channel> channelBeforeSave = channels.findBySlackId(SAMPLE_CHANNEL.getSlackId());
        Optional<Message> messageBeforeSave = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

        // when
        messageFileShareService.execute(fileShareRequest(expectedText));
        Optional<Channel> channelAfterSave = channels.findBySlackId(SAMPLE_CHANNEL.getSlackId());
        Optional<Message> messageAfterSave = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

        // then
        assertAll(
                () -> assertThat(channelBeforeSave).isPresent(),
                () -> assertThat(messageBeforeSave).isEmpty(),
                () -> assertThat(channelAfterSave).isPresent(),
                () -> assertThat(messageAfterSave).isPresent(),
                () -> assertThat(messageAfterSave.get().getText()).isEqualTo(expectedText)
        );
    }

    private String fileShareRequest(final String text) {
        Map<String, Object> request = Map.of("event", Map.of(
                "type", MESSAGE_FILE_SHARE.getType(),
                "subtype", MESSAGE_FILE_SHARE.getSubtype(),
                "files", new ArrayList<>(),
                "channel", SAMPLE_CHANNEL.getSlackId(),
                "text", text,
                "user", SAMPLE_MEMBER.getSlackId(),
                "ts", "1656919966.864259",
                "client_msg_id", SAMPLE_MESSAGE.getSlackId())
        );

        return toJson(request);
    }
}
