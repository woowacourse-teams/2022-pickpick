package com.pickpick.slackevent.application.message;

import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.MemberFixture.SUMMER;
import static com.pickpick.fixture.MessageFixtures.PLAIN_20220712_18_00_00;
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
class MessageCreatedServiceTest {

    @Autowired
    private MessageCreatedService messageCreatedService;

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

    @DisplayName("메시지 작성 이벤트 전달 시 메시지를 저장한다")
    @Test
    void saveMessageWhenMessageCreatedEventPassed() {
        // given
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Member summer = members.save(SUMMER.createLogin(jupjup));
        Channel notice = channels.save(NOTICE.create(jupjup));
        Message message = PLAIN_20220712_18_00_00.create(notice, summer);

        Optional<Channel> channelBeforeSave = channels.findBySlackId(notice.getSlackId());
        Optional<Message> messageBeforeSave = messages.findBySlackId(message.getSlackId());

        String messageCreatedRequest = createMessageCreatedRequest(notice, message, summer);

        // when
        messageCreatedService.execute(messageCreatedRequest);
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

    @DisplayName("메시지 댓글 생성 이벤트는 전달되어도 내용을 저장하지 않는다")
    @Test
    void doNotSaveReplyMessage() {
        //given
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Member summer = members.save(SUMMER.createLogin(jupjup));
        Channel notice = channels.save(NOTICE.create(jupjup));
        Message message = PLAIN_20220712_18_00_00.create(notice, summer);

        String messageRepliedRequest = createMessageRepliedRequest(notice, message, summer);

        // when
        messageCreatedService.execute(messageRepliedRequest);

        // then
        Optional<Message> messageAfterExecute = messages.findBySlackId(message.getSlackId());

        assertThat(messageAfterExecute).isEmpty();
    }

    private String createMessageCreatedRequest(final Channel channel, final Message message, final Member member) {
        return toJson(
                Map.of("event", Map.of(
                        "type", "message",
                        "channel", channel.getSlackId(),
                        "text", message.getText(),
                        "user", member.getSlackId(),
                        "ts", "1656919966.864259",
                        "client_msg_id", message.getSlackId())
                )
        );
    }

    private String createMessageRepliedRequest(final Channel channel, final Message message, final Member member) {
        return toJson(
                Map.of("event", Map.of(
                        "type", "message",
                        "channel", channel.getSlackId(),
                        "text", message.getText(),
                        "user", member.getSlackId(),
                        "ts", "1656919966.864259",
                        "client_msg_id", message.getSlackId(),
                        "thread_ts", "1234599999")
                )
        );
    }
}
