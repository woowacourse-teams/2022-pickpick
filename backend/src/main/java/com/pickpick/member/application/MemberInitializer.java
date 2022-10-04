package com.pickpick.member.application;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.support.SlackClient;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("prod")
@Component
public class MemberInitializer {

    private final SlackClient slackClient;
    private final MemberRepository memberRepository;

    public MemberInitializer(final SlackClient slackClient, final MemberRepository memberRepository) {
        this.slackClient = slackClient;
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    void setupMember() {
        List<String> savedSlackIds = findSavedSlackIds();
        List<Member> currentWorkspaceMembers = slackClient.findAllWorkspaceMembers();
        List<Member> membersToSave = filterMembersToSave(savedSlackIds, currentWorkspaceMembers);

        memberRepository.saveAll(membersToSave);
    }

    private List<String> findSavedSlackIds() {
        return memberRepository.findAll()
                .stream()
                .map(Member::getSlackId)
                .collect(Collectors.toList());
    }

    private List<Member> filterMembersToSave(final List<String> savedSlackIds,
                                             final List<Member> currentWorkspaceMembers) {
        return currentWorkspaceMembers.stream()
                .filter(it -> !savedSlackIds.contains(it.getSlackId()))
                .collect(Collectors.toList());
    }
}
