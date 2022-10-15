package com.pickpick.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pickpick.exception.member.MemberInvalidThumbnailUrlException;
import com.pickpick.exception.member.MemberInvalidUsernameException;
import com.pickpick.workspace.domain.Workspace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MemberTest {

    @DisplayName("유효하지 않은 username 일 시 예외 발생")
    @ParameterizedTest(name = "username: {0}")
    @NullAndEmptySource
    void updateInvalidUsername(final String username) {
        // given
        Workspace workspace = new Workspace("T00001", "xoxp-token-1234", "UB000001");
        Member member = new Member("U12345", "사용자", "test.png", workspace);

        // when & then
        assertThatThrownBy(() -> member.update(username, "test.png"))
                .isInstanceOf(MemberInvalidUsernameException.class);
    }

    @DisplayName("유효하지 않은 thumbnailUrl 일 시 예외 발생")
    @ParameterizedTest(name = "thumbnailUrl: {0}")
    @NullAndEmptySource
    void updateInvalidThumbnailUrl(final String thumbnailUrl) {
        // given
        Workspace workspace = new Workspace("T00001", "xoxp-token-1234", "UB000001");
        Member member = new Member("U12345", "사용자", "test.png", workspace);

        // when & then
        assertThatThrownBy(() -> member.update("사용자", thumbnailUrl))
                .isInstanceOf(MemberInvalidThumbnailUrlException.class);
    }
}
