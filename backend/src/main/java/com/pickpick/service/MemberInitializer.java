package com.pickpick.service;

import com.pickpick.entity.Member;
import com.pickpick.repository.MemberRepository;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberInitializer {

    @Value("${slack.bot-token}")
    private String slackBotToken;

    private final MethodsClient slackClient;
    private final MemberRepository memberRepository;

    public MemberInitializer(final MethodsClient slackClient, final MemberRepository memberRepository) {
        this.slackClient = slackClient;
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    void setupMember() throws SlackApiException, IOException {
        List<String> savedSlackIds = findSavedSlackIds();
        List<Member> currentWorkspaceMembers = getCurrentWorkspaceMembers();
        List<Member> membersToSave = filterMembersToSave(savedSlackIds, currentWorkspaceMembers);

        memberRepository.saveAll(membersToSave);
    }

    private List<String> findSavedSlackIds() {
        return memberRepository.findAll()
                .stream()
                .map(Member::getSlackId)
                .collect(Collectors.toList());
    }

    private List<Member> getCurrentWorkspaceMembers() throws IOException, SlackApiException {
        return toMembers(
                slackClient.usersList(request -> request.token(slackBotToken))
                        .getMembers());
    }

    private List<Member> filterMembersToSave(final List<String> savedSlackIds, final List<Member> currentWorkspaceMembers) {
        return currentWorkspaceMembers.stream()
                .filter(it -> !savedSlackIds.contains(it.getSlackId()))
                .collect(Collectors.toList());
    }

    private List<Member> toMembers(final List<User> users) {
        return users.stream()
                .map(this::toMember)
                .collect(Collectors.toList());
    }

    private Member toMember(final User user) {
        return new Member(user.getId(), user.getProfile().getDisplayName(), user.getProfile().getImage512());
    }
}
