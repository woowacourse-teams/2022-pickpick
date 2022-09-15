package com.pickpick.channel.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.channel.ui.dto.ChannelResponses;
import com.pickpick.config.DatabaseCleaner;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ChannelServiceTest {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private MemberRepository members;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("전체 채널 목록을 조회한다.")
    @Test
    void selectAllChannels() {
        // given
        Member member = saveMember();
        Channel channel1 = saveChannel("slackId1", "공지사항");
        Channel channel2 = saveChannel("slackId2", "잡담 게시판");

        // when
        ChannelResponses responses = channelService.findAll(member.getId());

        // then
        List<Long> actualIds = responses.getChannels()
                .stream()
                .map(ChannelResponse::getId)
                .collect(Collectors.toList());
        assertThat(actualIds).containsExactly(channel1.getId(), channel2.getId());
    }

    private Member saveMember() {
        return members.save(new Member("TESTMEMBER", "테스트 계정", "test.png"));
    }

    private Channel saveChannel(final String slackId, final String channelName) {
        return channels.save(new Channel(slackId, channelName));
    }
}
