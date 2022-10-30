package com.pickpick.message.application;

import com.pickpick.channel.domain.ChannelSubscription;
import com.pickpick.channel.domain.ChannelSubscriptionRepository;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.QMessageRepository;
import com.pickpick.message.support.SlackIdExtractor;
import com.pickpick.message.ui.dto.MessageRequest;
import com.pickpick.message.ui.dto.MessageResponse;
import com.pickpick.message.ui.dto.MessageResponses;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MessageService {

    private static final String MENTION_PREFIX = "<@";
    private static final String MENTION_SUFFIX = ">";
    private static final String MENTION_MARK = "@";

    private final MemberRepository members;
    private final ChannelSubscriptionRepository channelSubscriptions;
    private final QMessageRepository messages;
    private final SlackIdExtractor slackIdExtractor;

    public MessageService(final MemberRepository members,
                          final ChannelSubscriptionRepository channelSubscriptions,
                          final QMessageRepository messages,
                          final SlackIdExtractor slackIdExtractor) {
        this.members = members;
        this.messages = messages;
        this.channelSubscriptions = channelSubscriptions;
        this.slackIdExtractor = slackIdExtractor;
    }

    @MentionIdReplaceable
    public MessageResponses find(final Long memberId, final MessageRequest messageRequest) {
        List<Long> channelIds = findChannelId(memberId, messageRequest);

        List<MessageResponse> messageResponses = findMessages(memberId, channelIds, messageRequest);
        boolean hasPast = hasPast(channelIds, messageRequest, messageResponses);
        boolean hasFuture = hasFuture(channelIds, messageRequest, messageResponses);

        return new MessageResponses(messageResponses, hasPast, hasFuture, messageRequest.isNeedPastMessage());
    }

    private List<Long> findChannelId(final Long memberId, final MessageRequest messageRequest) {
        List<Long> channelIds = messageRequest.getChannelIds();

        if (isNonNullAndNotEmpty(channelIds)) {
            return channelIds;
        }

        ChannelSubscription firstSubscription = channelSubscriptions.getFirstByMemberIdOrderByViewOrderAsc(memberId);

        return List.of(firstSubscription.getChannelId());
    }

    private boolean isNonNullAndNotEmpty(final List<Long> channelIds) {
        return channelIds != null && !channelIds.isEmpty();
    }

    private List<MessageResponse> findMessages(final Long memberId, final List<Long> channelIds,
                                               final MessageRequest messageRequest) {
        boolean needPastMessage = messageRequest.isNeedPastMessage();

        List<MessageResponse> messageResponses = messages.findMessages(
                memberId,
                channelIds,
                messageRequest.getKeyword(),
                messageRequest.getMessageId(),
                messageRequest.getDate(),
                messageRequest.isNeedPastMessage(),
                messageRequest.getMessageCount());

        //replaceMentionMembers(memberId, messageResponses);

        if (needPastMessage) {
            return messageResponses;
        }

        return messageResponses.stream()
                .sorted(Comparator.comparing(MessageResponse::getPostedDate).reversed())
                .collect(Collectors.toList());
    }

    private void replaceMentionMembers(final Long memberId, final List<MessageResponse> messageResponses) {
        Member member = members.getById(memberId);
        List<Member> workspaceMembers = members.findAllByWorkspace(member.getWorkspace());

        Map<String, String> memberNames = workspaceMembers.stream()
                .collect(Collectors.toMap(Member::getSlackId,
                        workspaceMember -> MENTION_MARK + workspaceMember.getUsername()));

        for (MessageResponse message : messageResponses) {
            String text = replaceMentionMemberInText(message.getText(), memberNames);
            message.replaceText(text);
        }
    }

    private String replaceMentionMemberInText(String text, final Map<String, String> memberMap) {
        Set<String> slackIds = slackIdExtractor.extract(text);
        for (String slackId : slackIds) {
            String mention = MENTION_PREFIX + slackId + MENTION_SUFFIX;
            text = text.replace(mention, memberMap.getOrDefault(slackId, mention));
        }
        return text;
    }

    private boolean hasPast(final List<Long> channelIds, final MessageRequest messageRequest,
                            final List<MessageResponse> messages) {
        if (messages.isEmpty()) {
            return false;
        }

        MessageResponse message = messages.get(messages.size() - 1);
        return this.messages.existsByChannelsBeforePostedDate(channelIds, messageRequest, message);
    }

    private boolean hasFuture(final List<Long> channelIds, final MessageRequest messageRequest,
                              final List<MessageResponse> messages) {
        if (messages.isEmpty()) {
            return false;
        }

        MessageResponse message = messages.get(0);
        return this.messages.existsByChannelsAfterPostedDate(channelIds, messageRequest, message);
    }
}
