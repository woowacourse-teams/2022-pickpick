package com.pickpick.slackevent.application.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.message.dto.SlackMessageDto;
import com.pickpick.utils.TimeUtils;
import com.slack.api.RequestConfigurator;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsInfoRequest.ConversationsInfoRequestBuilder;
import com.slack.api.methods.response.conversations.ConversationsInfoResponse;
import com.slack.api.model.Conversation;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
class MessageThreadBroadcastServiceTest {

    private static final int FIRST_INDEX = 0;
    private final Member SAMPLE_MEMBER = new Member("U03MKN0UW", "사용자", "test.png");
    private final Channel SAMPLE_CHANNEL = new Channel("ASDFB", "채널");
    private final Message SAMPLE_MESSAGE = new Message(
            "db8a1f84-8acf-46ab-b93d-85177cee3e96",
            "메시지 전송!",
            SAMPLE_MEMBER,
            SAMPLE_CHANNEL,
            TimeUtils.toLocalDateTime("1234567890"),
            TimeUtils.toLocalDateTime("1234567890")
    );
    private final Map<String, Object> MESSAGE_THREAD_BROADCAST_REQUEST =
            Map.of("event", Map.of(
                            "type", SlackEvent.MESSAGE_THREAD_BROADCAST.getType(),
                            "subtype", SlackEvent.MESSAGE_THREAD_BROADCAST.getSubtype(),
                            "channel", SAMPLE_CHANNEL.getSlackId(),
                            "text", SAMPLE_MESSAGE.getText(),
                            "user", SAMPLE_MEMBER.getSlackId(),
                            "ts", "1234567890",
                            "client_msg_id", SAMPLE_MESSAGE.getSlackId()
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

    @MockBean
    private MethodsClient slackClient;

    @DisplayName("스레드 메시지 채널로 전송 이벤트 전달 시 채널이 없으면 채널 생성 후 메시지를 저장한다")
    @Test
    void saveChannelAndMessageDynamicallyWhenDoesNotExist() throws SlackApiException, IOException {
        // given
        members.save(SAMPLE_MEMBER);
        Optional<Channel> channelBeforeSave = channels.findBySlackId(SAMPLE_CHANNEL.getSlackId());
        Optional<Message> messageBeforeSave = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

        ConversationsInfoResponse conversationsInfoResponse = setupMockData();

        when(slackClient.conversationsInfo((RequestConfigurator<ConversationsInfoRequestBuilder>) any()))
                .thenReturn(conversationsInfoResponse);

        // when
        messageThreadBroadcastService.execute(MESSAGE_THREAD_BROADCAST_REQUEST);
        Optional<Channel> channelAfterSave = channels.findBySlackId(SAMPLE_CHANNEL.getSlackId());
        Optional<Message> messageAfterSave = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

        // then
        assertAll(
                () -> assertThat(channelBeforeSave).isEmpty(),
                () -> assertThat(messageBeforeSave).isEmpty(),
                () -> assertThat(channelAfterSave).isPresent(),
                () -> assertThat(messageAfterSave).isPresent()
        );
    }

    private ConversationsInfoResponse setupMockData() {
        Conversation conversation = new Conversation();
        conversation.setId(SAMPLE_CHANNEL.getSlackId());
        conversation.setName(SAMPLE_CHANNEL.getName());

        ConversationsInfoResponse conversationsInfoResponse = new ConversationsInfoResponse();
        conversationsInfoResponse.setChannel(conversation);

        return conversationsInfoResponse;
    }

    @DisplayName("스레드 메시지 채널로 전송 이벤트 전달 시 채널이 저장되어 있으면 채널 신규 저장 없이 메시지를 저장한다")
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
                "1234567890",
                "1234567890",
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
                "1234567890",
                "1234567890",
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
