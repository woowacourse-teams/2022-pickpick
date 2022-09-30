package com.pickpick.fixture;

import com.pickpick.member.domain.Member;

public enum MemberFixtures {

    SUMMER("U0000000001", "써머", "https://summer.png"),
    BOM("U0000000002", "봄", "https://bom.png"),
    YEONLOG("U0000000003", "연로그", "https://yeonlog.png"),
    HOPE("U0000000004", "호프", "https://hope.png"),
    KKOJAE("U0000000001", "꼬재", "https://kkojae.png"),
    ;

    private final String slackId;
    private final String username;
    private final String thumbnailUrl;

    MemberFixtures(final String slackId, final String username, final String userThumbnail) {
        this.slackId = slackId;
        this.username = username;
        this.thumbnailUrl = userThumbnail;
    }

    public Member create() {
        return new Member(slackId, username, thumbnailUrl);
    }
}
