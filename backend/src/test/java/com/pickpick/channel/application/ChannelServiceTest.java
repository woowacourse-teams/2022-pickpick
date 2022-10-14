package com.pickpick.channel.application;

import static com.pickpick.fixture.ChannelFixture.FREE_CHAT;
import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.ChannelFixture.QNA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.channel.domain.ChannelSubscription;
import com.pickpick.channel.domain.ChannelSubscriptionRepository;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.fixture.MemberFixture;
import com.pickpick.fixture.WorkspaceFixture;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import com.slack.api.RequestConfigurator;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsListRequest.ConversationsListRequestBuilder;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.model.Conversation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ChannelServiceTest {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private MemberRepository members;

    @Autowired
    private WorkspaceRepository workspaces;

    @Autowired
    private ChannelSubscriptionRepository channelSubscriptions;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @MockBean
    private MethodsClient methodsClient;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("전체 채널을 조회하면 모든 채널 목록과 각각의 구독 여부가 나온다")
    @Test
    void findAll() throws SlackApiException, IOException {
        // given
        Workspace workspace = workspaces.save(WorkspaceFixture.JUPJUP.create());
        Member yeonLog = saveMember(workspace);

        Channel notice = channels.save(NOTICE.create(workspace));
        Channel freeChat = channels.save(FREE_CHAT.create(workspace));
        Channel qna = channels.save(QNA.create(workspace));

        channelSubscriptions.save(new ChannelSubscription(freeChat, yeonLog, 1));

        given(methodsClient.conversationsList((RequestConfigurator<ConversationsListRequestBuilder>) any()))
                .willReturn(generateConversationsListResponse(notice, freeChat, qna));

        // when
        List<ChannelResponse> foundChannels = channelService.findByWorkspace(yeonLog.getId()).getChannels();
        List<Long> subscribedChannelIds = filterSubscribedChannelIds(foundChannels);
        List<Long> unsubscribedChannelIds = filterNotSubscribedChannelIds(foundChannels);

        // then
        assertAll(
                () -> assertThat(foundChannels).hasSize(3),
                () -> assertThat(subscribedChannelIds).containsExactly(freeChat.getId()),
                () -> assertThat(unsubscribedChannelIds).containsExactly(notice.getId(), qna.getId())
        );
    }

    @DisplayName("전체 채널을 조회하면 사용자가 입장한 채널만 조회된다")
    @Test
    void findChannelsHasUser() throws SlackApiException, IOException {
        // given
        Workspace workspace = workspaces.save(WorkspaceFixture.JUPJUP.create());
        Member yeonLog = saveMember(workspace);

        Channel notice = channels.save(NOTICE.create(workspace));
        Channel freeChat = channels.save(FREE_CHAT.create(workspace));
        Channel qna = channels.save(QNA.create(workspace));

        channelSubscriptions.save(new ChannelSubscription(freeChat, yeonLog, 1));

        given(methodsClient.conversationsList((RequestConfigurator<ConversationsListRequestBuilder>) any()))
                .willReturn(generateConversationsListResponse(notice));

        // when
        List<String> channelNames = channelService.findByWorkspace(yeonLog.getId())
                .getChannels()
                .stream()
                .map(ChannelResponse::getName)
                .collect(Collectors.toList());

        // then
        assertThat(channelNames).isNotEmpty()
                .doesNotContain(freeChat.getName(), qna.getName());
    }

    private Member saveMember(final Workspace workspace) {
        Member member = MemberFixture.YEONLOG.create(workspace);
        member.firstLogin("xoxp-token");
        return members.save(member);
    }

    private ConversationsListResponse generateConversationsListResponse(final Channel... channels) {
        ConversationsListResponse conversationsListResponse = new ConversationsListResponse();
        conversationsListResponse.setOk(true);
        List<Conversation> conversations = new ArrayList<>();
        for (Channel channel : channels) {
            Conversation conversation = new Conversation();
            conversation.setId(channel.getSlackId());
            conversation.setMember(true);
            conversations.add(conversation);
        }
        conversationsListResponse.setChannels(conversations);
        return conversationsListResponse;
    }

    private List<Long> filterSubscribedChannelIds(final List<ChannelResponse> foundChannels) {
        return foundChannels.stream()
                .filter(ChannelResponse::isSubscribed)
                .map(ChannelResponse::getId)
                .collect(Collectors.toList());
    }

    private List<Long> filterNotSubscribedChannelIds(final List<ChannelResponse> foundChannels) {
        return foundChannels.stream()
                .filter(channel -> !channel.isSubscribed())
                .map(ChannelResponse::getId)
                .collect(Collectors.toList());
    }
}
