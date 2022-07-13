package com.pickpick.controller;

import com.pickpick.entity.Member;
import com.pickpick.repository.MemberRepository;
import com.slack.api.Slack;
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

    private final MemberRepository memberRepository;

    public MemberInitializer(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    void setupMember() throws SlackApiException, IOException {
        List<String> existSlackIds = memberRepository.findAll()
                .stream()
                .map(Member::getSlackId)
                .collect(Collectors.toList());

        MethodsClient client = Slack.getInstance().methods();

        List<Member> foundMembers = toPickpickMembers(
                client.usersList(r -> r.token(slackBotToken))
                        .getMembers());

        List<Member> membersToSave = foundMembers.stream()
                .filter(it -> !existSlackIds.contains(it.getSlackId()))
                .collect(Collectors.toList());

        memberRepository.saveAll(membersToSave);
    }

    private List<Member> toPickpickMembers(final List<User> users) {
        return users.stream()
                .map(this::toPickpickMember)
                .collect(Collectors.toList());
    }

    private Member toPickpickMember(final User user) {
        return new Member(user.getId(), user.getProfile().getDisplayName(), user.getProfile().getImage512());
    }
}
