package com.pickpick.message.application;

import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.MemberFixture.BOM;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.fixture.MessageFixture;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.domain.Reminder;
import com.pickpick.message.domain.ReminderRepository;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
class ReminderSenderTest {

    @Autowired
    private WorkspaceRepository workspaces;

    @Autowired
    private MemberRepository members;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private ReminderRepository reminders;

    @Autowired
    private ReminderSender reminderSender;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @SpyBean
    private Clock clock;

    @DisplayName(value = "리마인드된 후에는 리마인더 목록에서 삭제된다")
    @Test
    void delete() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-11T23:50:00Z"));

        Workspace jupjup = workspaces.save(JUPJUP.create());
        Member bom = members.save(BOM.createLogin(jupjup));
        Channel notice = channels.save(NOTICE.create(jupjup));

        LocalDateTime now = now();
        saveMessagesAndReminders(notice, bom, now);

        // when
        reminderSender.sendRemindMessage();

        // then
        assertThat(reminders.findAllByRemindDate(now)).isEmpty();
    }

    private LocalDateTime now() {
        return LocalDateTime.now(clock)
                .withSecond(0)
                .withNano(0);
    }

    private void saveMessagesAndReminders(final Channel channel, final Member member,
                                          final LocalDateTime localDateTime) {
        List<Message> messagesInChannel = Arrays.stream(MessageFixture.values())
                .map(messageFixture -> messageFixture.create(channel, member))
                .collect(Collectors.toList());

        for (Message message : messagesInChannel) {
            messages.save(message);
            reminders.save(new Reminder(member, message, localDateTime));
        }
    }
}
