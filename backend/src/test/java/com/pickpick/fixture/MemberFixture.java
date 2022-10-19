package com.pickpick.fixture;

import com.pickpick.member.domain.Member;
import com.pickpick.workspace.domain.Workspace;

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

    public static String getMemberSlackIdByCode(final String memberCode) {
        return memberCode.replace("code", "");
    }

    public Member createLogin(final Workspace workspace) {
        Member member = new Member(slackId, username, thumbnailUrl, workspace);
        member.firstLogin("xoxp-" + slackId);
        return member;
    }

    public Member createNeverLoggedIn(final Workspace workspace) {
        return new Member(slackId, username, thumbnailUrl, workspace);
    }

    public String getCode() {
        return this.slackId + "code";
    }

    public String getSlackId() {
        return slackId;
    }
}
