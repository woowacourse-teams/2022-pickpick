package com.pickpick.controller;

import com.pickpick.entity.Member;
import com.pickpick.repository.MemberRepository;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.User;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class MemberInitializer {

    private final MemberRepository memberRepository;

    public MemberInitializer(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    void setupMember() throws SlackApiException, IOException {
        memberRepository.deleteAll();

        MethodsClient client = Slack.getInstance().methods();

        final List<Member> members = toPickpickMembers(
                client.usersList(r -> r.token("xoxb-3740298320131-3740551023445-ZYo41nYYQxrGPRT0BWnSB5dU"))
                        .getMembers());

        memberRepository.saveAll(members);
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
