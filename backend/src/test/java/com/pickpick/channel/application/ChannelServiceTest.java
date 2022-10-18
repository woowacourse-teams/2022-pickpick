package com.pickpick.channel.application;

import static com.pickpick.fixture.ChannelFixture.FREE_CHAT;
import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.ChannelFixture.QNA;
import static com.pickpick.fixture.MemberFixture.YEONLOG;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.channel.domain.ChannelSubscription;
import com.pickpick.channel.domain.ChannelSubscriptionRepository;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.fixture.StubSlack;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.support.ExternalClient;
import com.pickpick.support.TestConfig;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import com.slack.api.methods.SlackApiException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestConfig.class)
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

    @Autowired
    private ExternalClient externalClient;

    @Autowired
    private StubSlack stubSlack;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("전체 채널을 조회하면 모든 채널 목록과 각각의 구독 여부가 나온다")
    @Test
    void findAll() throws SlackApiException, IOException {
        // given
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Member yeonLog = members.save(YEONLOG.createLogin(jupjup));

        Channel notice = channels.save(NOTICE.create(jupjup));
        Channel freeChat = channels.save(FREE_CHAT.create(jupjup));
        Channel qna = channels.save(QNA.create(jupjup));

        stubSlack.setParticipatingChannel(yeonLog, notice, freeChat, qna);

        channelSubscriptions.save(new ChannelSubscription(freeChat, yeonLog, 1));

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
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Member yeonLog = members.save(YEONLOG.createLogin(jupjup));
        Channel notice = channels.save(NOTICE.create(jupjup));
        Channel freeChat = channels.save(FREE_CHAT.create(jupjup));
        Channel qna = channels.save(QNA.create(jupjup));

        channelSubscriptions.save(new ChannelSubscription(freeChat, yeonLog, 1));

        stubSlack.setParticipatingChannel(yeonLog, notice);

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
