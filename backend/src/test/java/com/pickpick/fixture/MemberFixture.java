package com.pickpick.fixture;

import com.pickpick.member.domain.Member;
import com.pickpick.workspace.domain.Workspace;
import java.util.Arrays;

public enum MemberFixture {

    SUMMER("U0000000001", "써머", "https://summer.png"),
    BOM("U0000000002", "봄", "https://bom.png"),
    YEONLOG("U0000000003", "연로그", "https://yeonlog.png"),
    HOPE("U0000000004", "호프", "https://hope.png"),
    KKOJAE("U0000000005", "꼬재", "https://kkojae.png"),
    ;

    private final String slackId;
    private final String username;
    private final String thumbnailUrl;

    MemberFixture(final String slackId, final String username, final String thumbnailUrl) {
        this.slackId = slackId;
        this.username = username;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static Member createFirst() {
        return Arrays.stream(MemberFixture.values())
                .findFirst()
                .orElse(SUMMER)
                .create();
    }

    // TODO 제거 필요
    public Member create() {
        return new Member(slackId, username, thumbnailUrl);
    }

    public Member create(final Workspace workspace) {
        Member member = new Member(slackId, username, thumbnailUrl, workspace);
        member.firstLogin("xoxp-" + slackId);
        return member;
    }

    public Member hasNeverLoggedIn(final Workspace workspace) {
        return new Member(slackId, username, thumbnailUrl, workspace);
    }

    public String getSlackId() {
        return slackId;
    }
}
