package com.pickpick.acceptance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.pickpick.entity.Member;
import com.pickpick.repository.MemberRepository;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.User;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("유저 기능")
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
public class MemberAcceptanceTest extends AcceptanceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 워크스페이스의_모든_유저_저장() throws SlackApiException, IOException {
        List<Member> members = 슬랙_api_호출();
        유저_목록_저장(members);
        유저가_저장되어있음();
    }

    private List<Member> 슬랙_api_호출() throws SlackApiException, IOException {
        MethodsClient client = Slack.getInstance().methods();
        List<User> users = client.usersList(
                        r -> r.token("xoxb-3740298320131-3740551023445-ZYo41nYYQxrGPRT0BWnSB5dU"))
                .getMembers();

        return toPickpickUsers(users);
    }

    private List<Member> toPickpickUsers(final List<User> users) {
        return users.stream()
                .map(this::toPickpickUser)
                .collect(Collectors.toList());
    }

    private Member toPickpickUser(final User user) {
        return new Member(user.getId(), user.getProfile().getDisplayName(), user.getProfile().getImage512());
    }

    private void 유저_목록_저장(final List<Member> members) {
        memberRepository.saveAll(members);
    }

    private void 유저가_저장되어있음() {
        List<Member> members = memberRepository.findAll();
        assertThat(members.isEmpty()).isFalse();
    }
}
