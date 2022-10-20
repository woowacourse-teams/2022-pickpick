package com.pickpick.message.application;

import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.MemberFixture.BOM;
import static com.pickpick.fixture.MemberFixture.HOPE;
import static com.pickpick.fixture.MemberFixture.KKOJAE;
import static com.pickpick.fixture.MemberFixture.SUMMER;
import static com.pickpick.fixture.MemberFixture.YEONLOG;
import static com.pickpick.fixture.MessageFixture.PLAIN_20220712_14_00_00;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.message.ReminderDeleteFailureException;
import com.pickpick.exception.message.ReminderNotFoundException;
import com.pickpick.exception.message.ReminderUpdateFailureException;
import com.pickpick.fixture.MessageFixture;
import com.pickpick.fixture.ReminderFindRequestFactory;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.domain.Reminder;
import com.pickpick.message.domain.ReminderRepository;
import com.pickpick.message.ui.dto.ReminderFindRequest;
import com.pickpick.message.ui.dto.ReminderResponse;
import com.pickpick.message.ui.dto.ReminderResponses;
import com.pickpick.message.ui.dto.ReminderSaveRequest;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReminderServiceTest {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private MemberRepository members;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private ReminderRepository reminders;

    @Autowired
    private WorkspaceRepository workspaces;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @DisplayName("리마인더 조회 시")
    @TestInstance(Lifecycle.PER_CLASS)
    @Nested
    class find {
        Workspace jupjup = workspaces.save(JUPJUP.create());
        Member bom = members.save(BOM.createLogin(jupjup));
        Member yeonlog = members.save(YEONLOG.createLogin(jupjup));
        Member summer = members.save(SUMMER.createLogin(jupjup));

        Channel notice = channels.save(NOTICE.create(jupjup));
        List<Message> noticeMessages = createAndSaveMessages(notice, yeonlog);

        List<Reminder> bomReminders = saveRemindersAtDifferentTimes(bom, noticeMessages);
        List<Reminder> yeonlogReminders = saveRemindersAtDifferentTimes(yeonlog, noticeMessages);
        int overTotalSize = bomReminders.size() + yeonlogReminders.size() + 5;

        @AfterAll
        void tearDown() {
            databaseCleaner.clear();
        }

        @DisplayName("해당 멤버의 리마인더만 조회된다")
        @Test
        void membersOwnReminders() {
            ReminderFindRequest request = ReminderFindRequestFactory.onlyCount(overTotalSize);
            List<ReminderResponse> foundReminders = reminderService.find(request, bom.getId())
                    .getReminders();

            List<Long> bomReminderIds = extractIds(bomReminders);
            List<Long> yeonlogReminderIds = extractIds(yeonlogReminders);

            assertAll(
                    () -> assertThat(foundReminders).extracting("id").containsAll(bomReminderIds),
                    () -> assertThat(foundReminders).extracting("id").doesNotContainAnyElementsOf(yeonlogReminderIds)
            );
        }

        @DisplayName("더 조회 할 리마인더가 없다면 hasFuture는 false다")
        @Test
        void noMoreRemindersToFindHasFutureFalse() {
            ReminderFindRequest request = ReminderFindRequestFactory.onlyCount(overTotalSize);
            ReminderResponses response = reminderService.find(request, bom.getId());

            assertThat(response.hasFuture()).isFalse();
        }

        @DisplayName("조회 할 리마인더가 남아있다면 hasFuture는 true다")
        @Test
        void moreRemindersToFindHasFutureTrue() {
            int lessThenTotal = bomReminders.size() - 1;
            ReminderFindRequest request = ReminderFindRequestFactory.onlyCount(lessThenTotal);
            ReminderResponses response = reminderService.find(request, bom.getId());

            assertThat(response.hasFuture()).isTrue();
        }

        @DisplayName("현재 시간보다 과거의 리마인더는 조회되지 않는다")
        @Test
        void excludePastReminders() {
            Message message = saveDummyMessage(bom, notice);
            LocalDateTime pastDateTime = LocalDateTime.now().minusMinutes(10);

            Reminder pastReminder = reminders.save(new Reminder(bom, message, pastDateTime));

            ReminderFindRequest request = ReminderFindRequestFactory.onlyCount(overTotalSize);
            List<ReminderResponse> foundReminders = reminderService.find(request, bom.getId())
                    .getReminders();

            assertAll(
                    () -> assertThat(foundReminders).isNotEmpty(),
                    () -> assertThat(foundReminders).extracting("id").doesNotContain(pastReminder.getId())
            );
        }

        @DisplayName("정렬 기준은 리마인드 시간 기준 오름차순이다")
        @Test
        void orderByRemindDateAsc() {
            ReminderFindRequest request = ReminderFindRequestFactory.onlyCount(overTotalSize);
            List<ReminderResponse> foundReminders = reminderService.find(request, bom.getId())
                    .getReminders();

            List<Long> expectedIds = extractIdsOrderedByRemindDate(bomReminders);

            assertThat(foundReminders).extracting("id").containsExactlyElementsOf(expectedIds);
        }

        @DisplayName("정렬 시 리마인드 시간이 같다면 id 오름차순 정렬된다")
        @Test
        void orderByIdWhenRemindDateIsSame() {
            Member hope = members.save(HOPE.createLogin(jupjup));
            Message firstMessage = saveDummyMessage(hope, notice);
            Message secondMessage = saveDummyMessage(hope, notice);

            LocalDateTime remindDate = LocalDateTime.now().plusHours(1);
            reminders.save(new Reminder(hope, firstMessage, remindDate));
            reminders.save(new Reminder(hope, secondMessage, remindDate));

            ReminderFindRequest request = ReminderFindRequestFactory.emptyQueryParams();
            List<ReminderResponse> foundReminders = reminderService.find(request, hope.getId())
                    .getReminders();

            assertThat(foundReminders.get(0).getId()).isLessThan(foundReminders.get(1).getId());
        }

        @DisplayName("요청 파라미터가 전부 없다면")
        @Nested
        class emptyParams {

            ReminderFindRequest request = ReminderFindRequestFactory.emptyQueryParams();

            @DisplayName("현재 시간에 가까운 리마인더부터 20개를 조회한다")
            @Test
            void twentyRemindersFromNow() {
                List<ReminderResponse> foundReminders = reminderService.find(request, bom.getId())
                        .getReminders();
                List<Long> expectedIds = extractIdsOrderedByRemindDateDefaultLimit(bomReminders);

                assertThat(foundReminders).extracting("id").containsExactlyElementsOf(expectedIds);
            }
        }

        @DisplayName("요청 파라미터에 개수만 있다면")
        @Nested
        class onlyCountInParmas {

            int count = 3;
            ReminderFindRequest request = ReminderFindRequestFactory.onlyCount(count);

            @DisplayName("해당 개수만큼 조회한다")
            @Test
            void limitCount() {
                List<ReminderResponse> foundReminders = reminderService.find(request, bom.getId())
                        .getReminders();

                assertThat(foundReminders).hasSize(count);
            }
        }

        @DisplayName("요청 파라미터에 리마인더 id만 있다면")
        @Nested
        class onlyReminderIdInParams {

            Reminder target = bomReminders.get(5);
            ReminderFindRequest request = ReminderFindRequestFactory.onlyReminderId(target.getId());

            @DisplayName("해당 리마인더보다 미래 시간의 리마인더를 기본 20개까지 조회한다")
            @Test
            void findFutureReminders() {
                List<ReminderResponse> foundReminders = reminderService.find(request, bom.getId())
                        .getReminders();
                List<Long> expectedIds = extractIdsFutureFromTargetOrderedByRemindDate(bomReminders, target, 20);

                assertThat(foundReminders).extracting("id").containsExactlyElementsOf(expectedIds);
            }
        }

        @DisplayName("요청 파라미터에 리마인더 id와 count가 있다면")
        @Nested
        class reminderIdAndCountInParams {

            Reminder target = bomReminders.get(5);
            int count = 3;

            ReminderFindRequest request = ReminderFindRequestFactory.reminderIdAndCount(target.getId(), count);

            @DisplayName("해당 리마인더보다 미래 시간의 리마인더를 count개 조회한다")
            @Test
            void findCountRemindersSetRemindedAfterTarget() {
                List<ReminderResponse> foundReminders = reminderService.find(request, bom.getId())
                        .getReminders();
                List<Long> expectedIds = extractIdsFutureFromTargetOrderedByRemindDate(bomReminders, target, count);

                assertThat(foundReminders).extracting("id").containsExactlyElementsOf(expectedIds);
            }
        }

        @DisplayName("messageId와 memberId로 단건 조회한다")
        @Test
        void findOne() {
            Reminder target = bomReminders.get(5);
            ReminderResponse response = reminderService.findOne(target.getMessage().getId(),
                    target.getMember().getId());

            assertThat(target.getId()).isEqualTo(response.getId());
        }

        @DisplayName("messageId와 memberId가 일치하는 리마인더가 없다면 예외가 발생한다")
        @Test
        void findOneThrowsException() {
            Member kkojae = members.save(KKOJAE.createLogin(jupjup));
            Message target = noticeMessages.get(0);

            assertThatThrownBy(() -> reminderService.findOne(target.getId(), kkojae.getId()))
                    .isInstanceOf(ReminderNotFoundException.class);
        }

        private List<Message> createAndSaveMessages(final Channel channel, final Member member) {
            List<Message> messagesInChannel = Arrays.stream(MessageFixture.values())
                    .map(messageFixture -> messageFixture.create(channel, member))
                    .collect(Collectors.toList());

            for (Message fixture : messagesInChannel) {
                messages.save(fixture);
            }

            return messagesInChannel;
        }

        private List<Reminder> saveRemindersAtDifferentTimes(final Member member, final List<Message> messages) {
            List<Reminder> savedReminders = new ArrayList<>();
            int totalSize = messages.size();

            for (int i = 0; i < totalSize; i++) {
                LocalDateTime remindDateTime = LocalDateTime.now().plusHours(i + 1);
                Reminder reminder = reminders.save(
                        new Reminder(member, messages.get(totalSize - i - 1), remindDateTime));
                savedReminders.add(reminder);
            }

            return savedReminders;
        }

        private Message saveDummyMessage(final Member member, final Channel channel) {
            Message message = new Message(UUID.randomUUID().toString(), "리마인더 설정 용 메시지", member, channel,
                    LocalDateTime.now(), LocalDateTime.now());
            return messages.save(message);
        }

        private List<Long> extractIds(final List<Reminder> reminders) {
            return reminders.stream()
                    .map(Reminder::getId)
                    .collect(Collectors.toList());
        }

        private List<Long> extractIdsOrderedByRemindDate(final List<Reminder> reminders) {
            return reminders.stream()
                    .sorted(Comparator.comparing(Reminder::getRemindDate))
                    .map(Reminder::getId)
                    .collect(Collectors.toList());
        }

        private List<Long> extractIdsOrderedByRemindDateDefaultLimit(final List<Reminder> reminders) {
            return reminders.stream()
                    .sorted(Comparator.comparing(Reminder::getRemindDate))
                    .map(Reminder::getId)
                    .limit(20)
                    .collect(Collectors.toList());
        }

        private List<Long> extractIdsFutureFromTargetOrderedByRemindDate(final List<Reminder> reminders,
                                                                         final Reminder target, final int limit) {
            return reminders.stream()
                    .filter(reminder -> reminder.getRemindDate().isAfter(target.getRemindDate()))
                    .sorted(Comparator.comparing(Reminder::getRemindDate))
                    .map(Reminder::getId)
                    .limit(limit)
                    .collect(Collectors.toList());
        }
    }

    @DisplayName("리마인더 저장/수정/삭제 시")
    @Nested
    class saveUpdateDelete {

        @AfterEach
        void tearDown() {
            databaseCleaner.clear();
        }

        @DisplayName("리마인더를 저장한다")
        @Test
        void save() {
            // given
            Workspace jupjup = workspaces.save(JUPJUP.create());
            Member yeonlog = members.save(YEONLOG.createLogin(jupjup));
            Channel notice = channels.save(NOTICE.create(jupjup));
            Message message = messages.save(PLAIN_20220712_14_00_00.create(notice, yeonlog));

            int beforeSize = findReminderSize(yeonlog);

            // when
            ReminderSaveRequest request = new ReminderSaveRequest(message.getId(),
                    LocalDateTime.now().plusHours(1));
            reminderService.save(yeonlog.getId(), request);

            // then
            int afterSize = findReminderSize(yeonlog);
            assertThat(beforeSize + 1).isEqualTo(afterSize);
        }

        @DisplayName("리마인더 수정 시 알람 시각이 변경된다")
        @Test
        void update() {
            // given
            Workspace jupjup = workspaces.save(JUPJUP.create());
            Member yeonlog = members.save(YEONLOG.createLogin(jupjup));
            Channel notice = channels.save(NOTICE.create(jupjup));
            Message message = messages.save(PLAIN_20220712_14_00_00.create(notice, yeonlog));

            LocalDateTime originTime = LocalDateTime.now().plusHours(1);
            reminders.save(new Reminder(yeonlog, message, originTime));

            // when
            LocalDateTime updateTime = LocalDateTime.now().plusDays(1);
            reminderService.update(yeonlog.getId(), new ReminderSaveRequest(message.getId(), updateTime));

            // then
            Reminder actual = reminders.getByMessageIdAndMemberId(message.getId(), yeonlog.getId());
            assertThat(actual.getRemindDate()).isEqualTo(updateTime);
        }

        @DisplayName("수정 시 memberId, messageId가 둘 다 일치하는 리마인더가 없다면 예외가 발생한다")
        @Test
        void updateReminderDoesNotExistThrowsException() {
            // given
            Workspace jupjup = workspaces.save(JUPJUP.create());
            Member yeonlog = members.save(YEONLOG.createLogin(jupjup));
            Member other = members.save(BOM.createLogin(jupjup));
            Channel notice = channels.save(NOTICE.create(jupjup));
            Message message = messages.save(PLAIN_20220712_14_00_00.create(notice, yeonlog));

            reminders.save(new Reminder(yeonlog, message, LocalDateTime.now().plusHours(1)));

            // when & then
            ReminderSaveRequest request = new ReminderSaveRequest(message.getId(),
                    LocalDateTime.now().plusHours(2));
            assertThatThrownBy(() -> reminderService.update(other.getId(), request))
                    .isInstanceOf(ReminderUpdateFailureException.class);
        }

        @DisplayName("리마인더를 단건 삭제한다")
        @Test
        void delete() {
            // given
            Workspace jupjup = workspaces.save(JUPJUP.create());
            Member yeonlog = members.save(YEONLOG.createLogin(jupjup));
            Channel notice = channels.save(NOTICE.create(jupjup));
            Message message = messages.save(PLAIN_20220712_14_00_00.create(notice, yeonlog));

            Reminder saved = reminders.save(new Reminder(yeonlog, message, LocalDateTime.now().plusHours(1)));

            // when
            reminderService.delete(message.getId(), yeonlog.getId());

            // then
            Optional<Reminder> afterDeleted = reminders.findById(saved.getId());
            assertThat(afterDeleted).isEmpty();
        }

        @DisplayName("삭제 시 memberId, messageId가 둘 다 일치하는 리마인더가 없다면 예외기 발생한다")
        @Test
        void deleteReminderDoesNotExistThrowsException() {
            // given
            Workspace jupjup = workspaces.save(JUPJUP.create());
            Member yeonlog = members.save(YEONLOG.createLogin(jupjup));
            Member other = members.save(BOM.createLogin(jupjup));
            Channel notice = channels.save(NOTICE.create(jupjup));
            Message message = messages.save(PLAIN_20220712_14_00_00.create(notice, yeonlog));

            reminders.save(new Reminder(yeonlog, message, LocalDateTime.now().plusHours(1)));

            // when & then
            assertThatThrownBy(() -> reminderService.delete(message.getId(), other.getId()))
                    .isInstanceOf(ReminderDeleteFailureException.class);
        }

        private int findReminderSize(final Member member) {
            ReminderFindRequest request = ReminderFindRequestFactory.emptyQueryParams();
            return reminderService.find(request, member.getId())
                    .getReminders()
                    .size();
        }
    }

    @DisplayName("리마인드 메시지 내부 멘션 아이디는")
    @Nested
    class mentionMessageInReminder {

        @AfterEach
        void tearDown() {
            databaseCleaner.clear();
        }

        Workspace jupjup = workspaces.save(JUPJUP.create());

        Member kkojae = members.save(KKOJAE.createLogin(jupjup));
        Member hope = members.save(HOPE.createLogin(jupjup));
        Member summer = members.save(SUMMER.createLogin(jupjup));

        Channel notice = channels.save(NOTICE.create(jupjup));

        LocalDateTime postedDate = LocalDateTime.now();

        @DisplayName("멘션 아이디가 한 개만 존재하는 경우")
        @Nested
        class oneMentionIdExisted {
            Message message = saveMessageWithText("<@" + summer.getSlackId() + ">" + " 메시지 내용");
            Reminder hopesReminder = saverReminders(message, hope);

            @DisplayName("단건 조회 시 올바른 닉네임으로 대치하여 보여준다.")
            @Test
            void findOne() {
                ReminderResponse response = reminderService.findOne(message.getId(), hope.getId());

                assertThat(response.getText()).contains(summer.getUsername());
            }

            @DisplayName("다건 조회 시 올바른 닉네임으로 대치하여 보여준다.")
            @Test
            void find() {
                ReminderFindRequest request = ReminderFindRequestFactory.emptyQueryParams();
                ReminderResponses response = reminderService.find(request, hope.getId());

                List<ReminderResponse> foundReminders = response.getReminders();

                assertAll(
                        () -> assertThat(foundReminders).hasSize(1),
                        () -> assertThat(foundReminders.get(0).getText()).contains(summer.getUsername())
                );
            }

        }

        @DisplayName("같은 멘션 아이디가 여러 개 존재하는 경우")
        @Nested
        class sameMentionIdsExisted {
            Message message = saveMessageWithText("<@" + summer.getSlackId() + ">"
                    + "메시지 내용"
                    + "<@" + summer.getSlackId() + ">"
                    + "<@" + summer.getSlackId() + ">");
            Reminder hopesReminder = saverReminders(message, hope);

            @DisplayName("단건 조회 시 올바른 닉네임으로 대치하여 보여준다.")
            @Test
            void findOne() {
                ReminderResponse response = reminderService.findOne(message.getId(), hope.getId());

                assertThat(response.getText()).doesNotContain("<@");
            }

            @DisplayName("다건 조회 시 모두 동일한 닉네임으로 대치하여 보여준다.")
            @Test
            void find() {
                ReminderFindRequest request = ReminderFindRequestFactory.emptyQueryParams();
                ReminderResponses response = reminderService.find(request, hope.getId());

                List<ReminderResponse> foundReminders = response.getReminders();

                assertAll(
                        () -> assertThat(foundReminders).hasSize(1),
                        () -> assertThat(foundReminders.get(0).getText()).doesNotContain("<@"),
                        () -> assertThat(foundReminders.get(0).getText()).contains(summer.getUsername())
                );
            }
        }

        @DisplayName("다른 멘션 아이디가 여러 개 존재하는 경우")
        @Nested
        class differentMentionIdsExisted {
            Message message = saveMessageWithText("<@" + summer.getSlackId() + ">"
                    + "메시지 내용"
                    + "<@" + hope.getSlackId() + ">"
                    + "<@" + kkojae.getSlackId() + ">");
            Reminder hopesReminder = saverReminders(message, hope);

            @DisplayName("단건 조회 시 올바른 닉네임으로 대치하여 보여준다.")
            @Test
            void findOne() {
                ReminderResponse response = reminderService.findOne(message.getId(), hope.getId());

                assertThat(response.getText()).contains(summer.getUsername(), hope.getUsername(), kkojae.getUsername());
            }


            @DisplayName("다건 조회 시 모두 해당 닉네임으로 대치하여 보여준다.")
            @Test
            void find() {
                ReminderFindRequest request = ReminderFindRequestFactory.emptyQueryParams();
                ReminderResponses response = reminderService.find(request, hope.getId());

                List<ReminderResponse> foundReminders = response.getReminders();

                assertAll(
                        () -> assertThat(foundReminders).hasSize(1),
                        () -> assertThat(foundReminders.get(0).getText())
                                .contains(summer.getUsername(), hope.getUsername(), kkojae.getUsername())
                );
            }
        }

        @DisplayName("같은 멘션 아이디와 다른 멘션 아이디가 여러 개 존재하는 경우")
        @Nested
        class sameAndDifferentMentionIdsExisted {
            Message message = saveMessageWithText("<@" + summer.getSlackId() + ">"
                    + "메시지 내용"
                    + "<@" + summer.getSlackId() + ">"
                    + "<@" + kkojae.getSlackId() + ">");
            Reminder hopesReminder = saverReminders(message, hope);

            @DisplayName("단건 조회 시 모두 해당 닉네임으로 대치하여 보여준다.")
            @Test
            void findOne() {
                ReminderResponse response = reminderService.findOne(message.getId(), hope.getId());

                assertThat(response.getText()).contains(summer.getUsername(), kkojae.getUsername());
            }

            @DisplayName("다건 조회 시 모두 해당 닉네임으로 대치하여 보여준다.")
            @Test
            void find() {
                ReminderFindRequest request = ReminderFindRequestFactory.emptyQueryParams();
                ReminderResponses response = reminderService.find(request, hope.getId());

                List<ReminderResponse> foundReminders = response.getReminders();

                assertAll(
                        () -> assertThat(foundReminders).hasSize(1),
                        () -> assertThat(foundReminders.get(0).getText())
                                .contains(summer.getUsername(), kkojae.getUsername())
                );
            }
        }

        @DisplayName("멘션 아이디가 멤버 중에 존재하지 않는 경우")
        @Nested
        class mentionIdNotExisted {
            Message message = saveMessageWithText("<@UNOTEXISTED> 존재하지 않는 멤버의 슬랙아이디");
            Reminder hopesReminder = saverReminders(message, hope);

            @DisplayName("단건 조회 시 멘션 아이디를 그대로 보여준다.")
            @Test
            void findOne() {
                ReminderResponse response = reminderService.findOne(message.getId(), hope.getId());

                assertThat(response.getText()).contains("<@UNOTEXISTED>");
            }


            @DisplayName("다건 조회시 멘션 아이디를 그대로 보여준다.")
            @Test
            void find() {
                ReminderFindRequest request = ReminderFindRequestFactory.emptyQueryParams();
                ReminderResponses response = reminderService.find(request, hope.getId());

                List<ReminderResponse> reminderResponses = response.getReminders();

                assertAll(
                        () -> assertThat(reminderResponses).hasSize(1),
                        () -> assertThat(reminderResponses.get(0).getText()).contains("<@UNOTEXISTED>")
                );
            }
        }

        private Message saveMessageWithText(String text) {
            Message message = new Message(UUID.randomUUID().toString(), text, kkojae, notice, postedDate, postedDate);
            return messages.save(message);
        }

        private Reminder saverReminders(Message message, Member member) {
            Reminder reminder = new Reminder(hope, message, postedDate.plusHours(1));
            return reminders.save(reminder);
        }
    }
}
