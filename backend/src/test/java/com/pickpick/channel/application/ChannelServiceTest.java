package com.pickpick.channel.application;

import static com.pickpick.fixture.ChannelFixture.FREE_CHAT;
import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.ChannelFixture.QNA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.channel.domain.ChannelSubscription;
import com.pickpick.channel.domain.ChannelSubscriptionRepository;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.support.DatabaseCleaner;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChannelServiceTest {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private MemberRepository members;

    @Autowired
    private ChannelSubscriptionRepository channelSubscriptions;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("전체 채널을 조회하면 모든 채널 목록과 각각의 구독 여부가 나온다")
    @Test
    void findAll() {
        // given
        Member yeonLog = members.save(new Member("U00001", "연로그", "https://yeonLog.png"));

        Channel notice = channels.save(NOTICE.create());
        Channel freeChat = channels.save(FREE_CHAT.create());
        Channel qna = channels.save(QNA.create());

        channelSubscriptions.save(new ChannelSubscription(freeChat, yeonLog, 1));

        // when
        List<ChannelResponse> foundChannels = channelService.findAll(yeonLog.getId()).getChannels();
        List<Long> subscribedChannelIds = filterSubscribedChannelIds(foundChannels);
        List<Long> unsubscribedChannelIds = filterNotSubscribedChannelIds(foundChannels);

        // then
        assertAll(
                () -> assertThat(foundChannels).hasSize(3),
                () -> assertThat(subscribedChannelIds).containsExactly(freeChat.getId()),
                () -> assertThat(unsubscribedChannelIds).containsExactly(notice.getId(), qna.getId())
        );
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
