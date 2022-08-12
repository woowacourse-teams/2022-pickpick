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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class MessageCreatedServiceTest {

    private static final Member SAMPLE_MEMBER = new Member("U03MKN0UW", "사용자", "test.png");
    private static final Channel SAMPLE_CHANNEL = new Channel("ASDFB", "채널");
    private static final Message SAMPLE_MESSAGE = new Message(
            "db8a1f84-8acf-46ab-b93d-85177cee3e97",
            "메시지 전송!",
            SAMPLE_MEMBER,
            SAMPLE_CHANNEL,
            TimeUtils.toLocalDateTime("1234567890"),
            TimeUtils.toLocalDateTime("1234567890")
    );
    private static final Map<String, Object> MESSAGE_CREATED_REQUEST =
            Map.of("event", Map.of(
                    "type", "message",
                    "channel", SAMPLE_CHANNEL.getSlackId(),
                    "text", SAMPLE_MESSAGE.getText(),
                    "user", SAMPLE_MEMBER.getSlackId(),
                    "ts", "1234567890",
                    "client_msg_id", SAMPLE_MESSAGE.getSlackId())
            );
    private static final Map<String, Object> MESSAGE_REPLIED_REQUEST =
            Map.of("event", Map.of(
                    "type", "message",
                    "channel", SAMPLE_CHANNEL.getSlackId(),
                    "text", SAMPLE_MESSAGE.getText(),
                    "user", SAMPLE_MEMBER.getSlackId(),
                    "ts", "1234567890",
                    "client_msg_id", SAMPLE_MESSAGE.getSlackId(),
                    "thread_ts", "1234599999")
            );
    private static final int FIRST_INDEX = 0;

    @Autowired
    private MessageCreatedService messageCreatedService;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private MemberRepository members;

    @Autowired
    private ChannelRepository channels;

    @MockBean
    private MethodsClient slackClient;

    @DisplayName("메시지 작성 이벤트 전달 시 채널이 없으면 채널 생성 후 메시지를 저장한다")
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
        messageCreatedService.execute(MESSAGE_CREATED_REQUEST);
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

    @DisplayName("메시지 작성 이벤트 전달 시 채널이 저장되어 있으면 채널 신규 저장 없이 메시지를 저장한다")
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
