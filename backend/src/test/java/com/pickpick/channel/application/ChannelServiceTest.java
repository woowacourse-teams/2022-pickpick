package com.pickpick.channel.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.SlackApiCallException;
import com.slack.api.RequestConfigurator;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsInfoRequest.ConversationsInfoRequestBuilder;
import com.slack.api.methods.response.conversations.ConversationsInfoResponse;
import com.slack.api.model.Conversation;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ChannelServiceTest {

    @MockBean
    private MethodsClient slackClient;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private ChannelService channelService;

    @DisplayName("채널을 저장한다.")
    @Test
    void save() throws SlackApiException, IOException {
        // given
        given(slackClient.conversationsInfo((RequestConfigurator<ConversationsInfoRequestBuilder>) any()))
                .willReturn(setUpChannelMockData());

        Optional<Channel> channelBeforeExecute = channels.findBySlackId("channelSlackId");

        // when
        channelService.createChannel("channelSlackId");

        // then
        Optional<Channel> channelAfterExecute = channels.findBySlackId("channelSlackId");
        assertAll(
                () -> assertThat(channelBeforeExecute).isEmpty(),
                () -> assertThat(channelAfterExecute).isPresent()
        );
    }

    @DisplayName("채널 저장 시 Exception이 발생하면 커스텀 예외인 SlackApiCallException을 호출한다")
    @Test
    void saveFailAndThrowCustomException() throws SlackApiException, IOException {
        // given
        given(slackClient.conversationsInfo((RequestConfigurator<ConversationsInfoRequestBuilder>) any()))
                .willThrow(SlackApiException.class);

        // when & then
        assertThatThrownBy(() -> channelService.createChannel("channelSlackId"))
                .isInstanceOf(SlackApiCallException.class);
    }

    private ConversationsInfoResponse setUpChannelMockData() {
        Conversation conversation = new Conversation();
        conversation.setId("channelSlackId");
        conversation.setName("channelName");

        ConversationsInfoResponse conversationsInfoResponse = new ConversationsInfoResponse();
        conversationsInfoResponse.setChannel(conversation);

        return conversationsInfoResponse;
    }
}
