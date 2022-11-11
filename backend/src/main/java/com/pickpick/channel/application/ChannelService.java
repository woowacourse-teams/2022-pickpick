package com.pickpick.channel.application;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.channel.domain.ChannelSubscription;
import com.pickpick.channel.domain.ChannelSubscriptionRepository;
import com.pickpick.channel.ui.dto.ChannelResponse;
import com.pickpick.channel.ui.dto.ChannelResponses;
import com.pickpick.exception.member.MemberTokenNotFoundException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.slackevent.domain.Participation;
import com.pickpick.support.ExternalClient;
import com.pickpick.support.MentionIdReplaceable;
import com.pickpick.workspace.domain.Workspace;
import com.querydsl.core.util.StringUtils;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ChannelService {

    private final ExternalClient externalClient;
    private final MemberRepository members;
    private final ChannelRepository channels;
    private final ChannelSubscriptionRepository channelSubscriptions;

    public ChannelService(final ExternalClient externalClient, final MemberRepository members,
                          final ChannelRepository channels, final ChannelSubscriptionRepository channelSubscriptions) {
        this.externalClient = externalClient;
        this.members = members;
        this.channels = channels;
        this.channelSubscriptions = channelSubscriptions;
    }

    @MentionIdReplaceable
    public ChannelResponses findByWorkspace(final Long memberId) {
        Member member = members.getById(memberId);
        validateToken(member);

        List<Channel> participatingChannels = findParticipatingChannels(member);
        Set<Channel> subscribedChannels = findSubscribedChannels(memberId);

        List<ChannelResponse> channelResponses = generateChannelResponses(participatingChannels, subscribedChannels);
        return new ChannelResponses(channelResponses);
    }

    private void validateToken(final Member member) {
        String token = member.getSlackToken();
        if (StringUtils.isNullOrEmpty(token)) {
            throw new MemberTokenNotFoundException(member.getId(), token);
        }
    }

    private List<Channel> findParticipatingChannels(final Member member) {
        String token = member.getSlackToken();
        Participation participation = externalClient.findChannelParticipation(token);

        Workspace workspace = member.getWorkspace();
        return channels.findAllByWorkspaceOrderByName(workspace)
                .stream()
                .filter(it -> participation.isParticipant(it.getSlackId()))
                .collect(Collectors.toList());
    }

    private Set<Channel> findSubscribedChannels(final Long memberId) {
        return channelSubscriptions.findAllByMemberId(memberId)
                .stream()
                .map(ChannelSubscription::getChannel)
                .collect(Collectors.toSet());
    }

    private List<ChannelResponse> generateChannelResponses(final List<Channel> allChannels,
                                                           final Set<Channel> subscribedChannels) {
        return allChannels.stream()
                .map(channel -> ChannelResponse.of(subscribedChannels, channel))
                .collect(Collectors.toList());
    }
}
