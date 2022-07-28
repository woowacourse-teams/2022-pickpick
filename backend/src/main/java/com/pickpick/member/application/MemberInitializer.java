package com.pickpick.member.application;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.User;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MemberInitializer {

    private final MethodsClient client = Slack.getInstance().methods();
    private final MemberRepository memberRepository;
    @Value("${slack.bot-token}")
    private String slackBotToken;

    public MemberInitializer(final MemberRepository memberRepository) {
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
                client.usersList(request -> request.token(slackBotToken))
                        .getMembers());
    }

    private List<Member> filterMembersToSave(final List<String> savedSlackIds,
                                             final List<Member> currentWorkspaceMembers) {
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
