package com.pickpick.slackevent.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.slackevent.application.channel.ChannelCreatedService;
import com.pickpick.slackevent.application.channel.ChannelDeletedService;
import com.pickpick.slackevent.application.channel.ChannelRenameService;
import com.pickpick.slackevent.application.member.MemberChangedService;
import com.pickpick.slackevent.application.member.MemberJoinService;
import com.pickpick.slackevent.application.message.MessageChangedService;
import com.pickpick.slackevent.application.message.MessageCreatedService;
import com.pickpick.slackevent.application.message.MessageDeletedService;
import com.pickpick.slackevent.application.message.MessageFileShareService;
import com.pickpick.slackevent.application.message.MessageThreadBroadcastService;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SlackEventServiceFinderTest {

    @Autowired
    private SlackEventServiceFinder slackEventServiceFinder;

    @DisplayName(value = "SlackEvent 타입에 따라 적절한 서비스를 찾는다")
    @ParameterizedTest
    @MethodSource("sources")
    void findService(final SlackEvent slackEvent, final Class<?> expected) {
        // given & when
        SlackEventService slackEventService = slackEventServiceFinder.findBySlackEvent(slackEvent);

        // then
        assertThat(slackEventService).isInstanceOf(expected);
    }

    private static Stream<Arguments> sources() {
        return Stream.of(
                Arguments.arguments(SlackEvent.MEMBER_JOIN, MemberJoinService.class),
                Arguments.arguments(SlackEvent.MEMBER_CHANGED, MemberChangedService.class),
                Arguments.arguments(SlackEvent.MESSAGE_CREATED, MessageCreatedService.class),
                Arguments.arguments(SlackEvent.MESSAGE_THREAD_BROADCAST, MessageThreadBroadcastService.class),
                Arguments.arguments(SlackEvent.MESSAGE_FILE_SHARE, MessageFileShareService.class),
                Arguments.arguments(SlackEvent.MESSAGE_CHANGED, MessageChangedService.class),
                Arguments.arguments(SlackEvent.MESSAGE_DELETED, MessageDeletedService.class),
                Arguments.arguments(SlackEvent.CHANNEL_CREATED, ChannelCreatedService.class),
                Arguments.arguments(SlackEvent.CHANNEL_RENAME, ChannelRenameService.class),
                Arguments.arguments(SlackEvent.CHANNEL_DELETED, ChannelDeletedService.class)
        );
    }
}
