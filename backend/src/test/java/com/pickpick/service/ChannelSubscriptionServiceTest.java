package com.pickpick.service;

import com.pickpick.entity.Channel;
import com.pickpick.entity.ChannelSubscription;
import com.pickpick.repository.ChannelRepository;
import com.pickpick.repository.ChannelSubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChannelSubscriptionServiceTest {

    @Autowired
    private ChannelSubscriptionService channelSubscriptionService;

    @Autowired
    private ChannelSubscriptionRepository channelSubscriptions;

    @Autowired
    private ChannelRepository channels;

    @BeforeEach
    void setup() {
        channelSubscriptions.deleteAll();
    }

    @DisplayName("채널 구독을 저장")
    @Test
    void saveAll() {
        // given
        List<Channel> findChannels = channels.findAll();

        // when
        channelSubscriptionService.saveAll(findChannels);

        // then
        assertThat(channelSubscriptionService.findAll().size()).isEqualTo(findChannels.size());
    }

    @DisplayName("채널 구독 전체를 올바른 순서로 조회")
    @Test
    void findAll() {
        //given
        List<Channel> findChannels = channels.findAll();

        channelSubscriptionService.saveAll(findChannels);

        List<ChannelSubscription> actual = channelSubscriptionService.findAll();

        for (int i = 0; i < actual.size(); i++) {
            assertThat(findChannels.get(i)).isEqualTo(actual.get(i).getChannel());
        }
    }
}
