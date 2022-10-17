package com.pickpick.member.domain;

import static com.pickpick.fixture.MemberFixture.BOM;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
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
        Workspace jupjup = JUPJUP.create();
        Member bom = BOM.createLogin(jupjup);

        // when & then
        assertThatThrownBy(() -> bom.update(username, "test.png"))
                .isInstanceOf(MemberInvalidUsernameException.class);
    }

    @DisplayName("유효하지 않은 thumbnailUrl 일 시 예외 발생")
    @ParameterizedTest(name = "thumbnailUrl: {0}")
    @NullAndEmptySource
    void updateInvalidThumbnailUrl(final String thumbnailUrl) {
        // given
        Workspace jupjup = JUPJUP.create();
        Member bom = BOM.createLogin(jupjup);

        // when & then
        assertThatThrownBy(() -> bom.update("사용자", thumbnailUrl))
                .isInstanceOf(MemberInvalidThumbnailUrlException.class);
    }
}
