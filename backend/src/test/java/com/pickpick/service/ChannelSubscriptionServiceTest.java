package com.pickpick.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.entity.Channel;
import com.pickpick.entity.ChannelSubscription;
import com.pickpick.entity.Member;
import com.pickpick.repository.ChannelRepository;
import com.pickpick.repository.MemberRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ChannelSubscriptionServiceTest {

    @Autowired
    private ChannelSubscriptionService channelSubscriptionService;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private MemberRepository members;

    @DisplayName("채널 구독을 저장")
    @Test
    void saveAll() {
        // given
        Member member = saveTestFixtureMember();
        List<Channel> findChannels = channels.findAll();

        // when
        channelSubscriptionService.saveAll(findChannels, member.getId());

        // then
        assertThat(channelSubscriptionService.findAll(member.getId()).size()).isEqualTo(findChannels.size());
    }

    @DisplayName("isSubscribed와 무관하게 구독 전체를 채널 이름 순서로 조회")
    @Test
    void findAll() {
        //given
        Member member = saveTestFixtureMember();
        List<Channel> findChannels = channels.findAll();
        List<Channel> expected = findChannels.stream().sorted(Comparator.comparing(Channel::getName))
                .collect(Collectors.toList());

        // when
        channelSubscriptionService.saveAll(findChannels, member.getId());

        // then
        List<ChannelSubscription> actual = channelSubscriptionService.findAll(member.getId());

        for (int i = 0; i < actual.size(); i++) {
            assertThat(expected.get(i)).isEqualTo(actual.get(i).getChannel());
        }
    }

    private Member saveTestFixtureMember() {
        Member member = new Member("TESTMEMBER", "테스트 계정", "test.png");
        members.save(member);
        return member;
    }
}
