package com.pickpick.slackevent.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.pickpick.slackevent.application.channel.ChannelCreatedEventHandler;
import com.pickpick.slackevent.application.channel.ChannelDeletedEventHandler;
import com.pickpick.slackevent.application.channel.ChannelRenameEventHandler;
import com.pickpick.slackevent.application.member.MemberChangedEventHandler;
import com.pickpick.slackevent.application.member.MemberJoinEventHandler;
import com.pickpick.slackevent.application.message.MessageChangedEventHandler;
import com.pickpick.slackevent.application.message.MessageCreatedEventHandler;
import com.pickpick.slackevent.application.message.MessageDeletedEventHandler;
import com.pickpick.slackevent.application.message.MessageFileShareEventHandler;
import com.pickpick.slackevent.application.message.MessageThreadBroadcastEventHandler;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SlackEventHandlerFinderTest {

    @Autowired
    private SlackEventHandlerFinder slackEventHandlerFinder;

    @DisplayName(value = "SlackEvent 타입에 따라 적절한 서비스를 찾는다")
    @ParameterizedTest
    @MethodSource("sources")
    void findService(final SlackEvent slackEvent, final Class<?> expected) {
        // given & when
        SlackEventHandler slackEventHandler = slackEventHandlerFinder.findBySlackEvent(slackEvent);

        // then
        assertThat(slackEventHandler).isInstanceOf(expected);
    }

    private static Stream<Arguments> sources() {
        return Stream.of(
                Arguments.arguments(SlackEvent.MEMBER_JOIN, MemberJoinEventHandler.class),
                Arguments.arguments(SlackEvent.MEMBER_CHANGED, MemberChangedEventHandler.class),
                Arguments.arguments(SlackEvent.MESSAGE_CREATED, MessageCreatedEventHandler.class),
                Arguments.arguments(SlackEvent.MESSAGE_THREAD_BROADCAST, MessageThreadBroadcastEventHandler.class),
                Arguments.arguments(SlackEvent.MESSAGE_FILE_SHARE, MessageFileShareEventHandler.class),
                Arguments.arguments(SlackEvent.MESSAGE_CHANGED, MessageChangedEventHandler.class),
                Arguments.arguments(SlackEvent.MESSAGE_DELETED, MessageDeletedEventHandler.class),
                Arguments.arguments(SlackEvent.CHANNEL_CREATED, ChannelCreatedEventHandler.class),
                Arguments.arguments(SlackEvent.CHANNEL_RENAME, ChannelRenameEventHandler.class),
                Arguments.arguments(SlackEvent.CHANNEL_DELETED, ChannelDeletedEventHandler.class)
        );
    }
}
