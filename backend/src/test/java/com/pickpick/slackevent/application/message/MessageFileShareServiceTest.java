package com.pickpick.slackevent.application.message;

import static com.pickpick.slackevent.application.SlackEvent.MESSAGE_FILE_SHARE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class MessageFileShareServiceTest {

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

    @Autowired
    private MessageFileShareService messageFileShareService;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private MemberRepository members;

    @Autowired
    private ChannelRepository channels;

    @MockBean
    private MethodsClient slackClient;

    @DisplayName("파일 공유 이벤트 전달 시 채널이 저장되어 있지 않으면 채널 신규 저장 후 메시지를 저장한다")
    @ValueSource(strings = {"", " ", "파일과 함께 전송한 메시지 text"})
    @ParameterizedTest
    void fileShareMessage(final String expectedText) throws SlackApiException, IOException {
        // given
        members.save(SAMPLE_MEMBER);
        Optional<Channel> channelBeforeSave = channels.findBySlackId(SAMPLE_CHANNEL.getSlackId());
        Optional<Message> messageBeforeSave = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

        ConversationsInfoResponse conversationsInfoResponse = setUpChannelMockData();

        given(slackClient.conversationsInfo((RequestConfigurator<ConversationsInfoRequestBuilder>) any()))
                .willReturn(conversationsInfoResponse);

        // when
        messageFileShareService.execute(fileShareRequest(expectedText));
        Optional<Channel> channelAfterSave = channels.findBySlackId(SAMPLE_CHANNEL.getSlackId());
        Optional<Message> messageAfterSave = messages.findBySlackId(SAMPLE_MESSAGE.getSlackId());

        // then
        assertAll(
                () -> assertThat(channelBeforeSave).isEmpty(),
                () -> assertThat(messageBeforeSave).isEmpty(),
                () -> assertThat(channelAfterSave).isPresent(),
                () -> assertThat(messageAfterSave).isPresent(),
                () -> assertThat(messageAfterSave.get().getText()).isEqualTo(expectedText)
        );
    }

    @DisplayName("파일 공유 이벤트 전달 시 채널이 저장되어 있으면 채널 신규 저장 없이 메시지를 저장한다")
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

    private ConversationsInfoResponse setUpChannelMockData() {
        Conversation conversation = new Conversation();
        conversation.setId(SAMPLE_CHANNEL.getSlackId());
        conversation.setName(SAMPLE_CHANNEL.getName());

        ConversationsInfoResponse conversationsInfoResponse = new ConversationsInfoResponse();
        conversationsInfoResponse.setChannel(conversation);

        return conversationsInfoResponse;
    }

    private Map<String, Object> fileShareRequest(final String text) {
        return Map.of("event", Map.of(
                "type", MESSAGE_FILE_SHARE.getType(),
                "subtype", MESSAGE_FILE_SHARE.getSubtype(),
                "files", new ArrayList<>(),
                "channel", SAMPLE_CHANNEL.getSlackId(),
                "text", text,
                "user", SAMPLE_MEMBER.getSlackId(),
                "ts", "1234567890",
                "client_msg_id", SAMPLE_MESSAGE.getSlackId())
        );
    }
}
