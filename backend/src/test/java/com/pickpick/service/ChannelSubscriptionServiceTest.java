package com.pickpick.service;

import com.pickpick.controller.dto.ChannelResponse;
import com.pickpick.entity.Channel;
import com.pickpick.entity.Member;
import com.pickpick.repository.ChannelRepository;
import com.pickpick.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

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
        Member member = saveMember();
        List<Channel> findChannels = channels.findAll();

        // when
        channelSubscriptionService.saveAll(findChannels, member.getId());

        // then
        assertThat(channelSubscriptionService.findAll(member.getId()).size()).isEqualTo(findChannels.size());
    }

    @DisplayName("모든 채널을 구독 여부 포함해 이름순 조회")
    @Test
    void findAll() {
        //given
        Member member = saveMember();
        List<Channel> findChannels = channels.findAllByOrderByName();
        List<Channel> expected = findChannels.stream().sorted(Comparator.comparing(Channel::getName))
                .collect(Collectors.toList());

        // when
        channelSubscriptionService.saveAll(findChannels, member.getId());

        // then
        List<ChannelResponse> actual = channelSubscriptionService.findAll(member.getId());

        for (int i = 0; i < actual.size(); i++) {
            assertThat(expected.get(i).getId()).isEqualTo(actual.get(i).getId());
        }
    }

    private Member saveMember() {
        Member member = new Member("TESTMEMBER", "테스트 계정", "test.png");
        members.save(member);
        return member;
    }
}
