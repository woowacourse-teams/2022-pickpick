package com.pickpick.slackevent.application.message;

import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.MemberFixture.SUMMER;
import static com.pickpick.fixture.MessageFixtures.PLAIN_20220712_14_00_00;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
import static com.pickpick.support.JsonUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;

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
class MessageDeletedServiceTest {

    @Autowired
    private MessageDeletedService messageDeletedService;

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

    @DisplayName("메시지 slack id가 전달되었을 때 메시지를 삭제한다.")
    @Test
    void deletedMessage() {
        // given
        Message storedMessage = saveMessage();
        Map<String, String> previousMessage = Map.of("client_msg_id", storedMessage.getSlackId());
        Map<String, Object> event = Map.of("previous_message", previousMessage);
        String request = toJson(Map.of("event", event));

        // when
        messageDeletedService.execute(request);

        // then
        Optional<Message> findMessage = messages.findBySlackId(storedMessage.getSlackId());
        assertThat(findMessage).isEmpty();
    }

    private Message saveMessage() {
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Member summer = members.save(SUMMER.createLogin(jupjup));
        Channel notice = channels.save(NOTICE.create(jupjup));

        return messages.save(PLAIN_20220712_14_00_00.create(notice, summer));
    }
}
