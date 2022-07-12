package com.pickpick.acceptance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.pickpick.entity.User;
import com.pickpick.repository.UserRepository;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
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
public class UserAcceptanceTest extends AcceptanceTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void 워크스페이스의_모든_유저_저장() throws SlackApiException, IOException {
        List<User> users = 슬랙_api_호출();
        유저_목록_저장(users);
        유저가_저장되어있음();
    }

    private List<User> 슬랙_api_호출() throws SlackApiException, IOException {
        MethodsClient client = Slack.getInstance().methods();
        List<com.slack.api.model.User> users = client.usersList(
                        r -> r.token("xoxb-3740298320131-3740551023445-ZYo41nYYQxrGPRT0BWnSB5dU"))
                .getMembers();

        return toPickpickUsers(users);
    }

    private List<User> toPickpickUsers(final List<com.slack.api.model.User> users) {
        return users.stream()
                .map(this::toPickpickUser)
                .collect(Collectors.toList());
    }

    private User toPickpickUser(final com.slack.api.model.User user) {
        return new User(user.getId(), user.getProfile().getDisplayName(), user.getProfile().getImage512());
    }

    private void 유저_목록_저장(final List<User> users) {
        userRepository.saveAll(users);
    }

    private void 유저가_저장되어있음() {
        List<User> users = userRepository.findAll();
        assertThat(users.isEmpty()).isFalse();
    }
}
