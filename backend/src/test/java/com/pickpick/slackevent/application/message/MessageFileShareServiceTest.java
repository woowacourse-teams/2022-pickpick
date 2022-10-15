package com.pickpick.slackevent.application.message;

import static com.pickpick.fixture.ChannelFixture.QNA;
import static com.pickpick.fixture.MemberFixture.BOM;
import static com.pickpick.fixture.MessageFixtures.PLAIN_20220715_17_00_00;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
import static com.pickpick.slackevent.application.SlackEvent.MESSAGE_FILE_SHARE;
import static com.pickpick.support.JsonUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
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

    @Autowired
    private MessageFileShareService messageFileShareService;

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

    @DisplayName("파일 공유 이벤트 전달 시 채널이 저장되어 있으면 메시지를 저장한다")
    @ValueSource(strings = {"", " ", "파일과 함께 전송한 메시지 text"})
    @ParameterizedTest
    void saveMessageWhenFileShareEventPassed(final String expectedText) {
        // given
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Member bom = members.save(BOM.create(jupjup));
        Channel qna = channels.save(QNA.create(jupjup));
        Message message = PLAIN_20220715_17_00_00.create(qna, bom);

        Optional<Channel> channelBeforeSave = channels.findBySlackId(qna.getSlackId());
        Optional<Message> messageBeforeSave = messages.findBySlackId(message.getSlackId());

        // when
        messageFileShareService.execute(fileShareRequest(message, expectedText));
        Optional<Channel> channelAfterSave = channels.findBySlackId(qna.getSlackId());
        Optional<Message> messageAfterSave = messages.findBySlackId(message.getSlackId());

        // then
        assertAll(
                () -> assertThat(channelBeforeSave).isPresent(),
                () -> assertThat(messageBeforeSave).isEmpty(),
                () -> assertThat(channelAfterSave).isPresent(),
                () -> assertThat(messageAfterSave).isPresent(),
                () -> assertThat(messageAfterSave.get().getText()).isEqualTo(expectedText)
        );
    }

    private String fileShareRequest(final Message message, final String text) {
        Map<String, Object> request = Map.of("event", Map.of(
                "type", MESSAGE_FILE_SHARE.getType(),
                "subtype", MESSAGE_FILE_SHARE.getSubtype(),
                "files", new ArrayList<>(),
                "channel", message.getChannel().getSlackId(),
                "text", text,
                "user", message.getMember().getSlackId(),
                "ts", "1656919966.864259",
                "client_msg_id", message.getSlackId())
        );

        return toJson(request);
    }
}
