package com.pickpick.fixture;

import com.pickpick.member.domain.Member;

public class MemberFactory {

    private static final String 써머_슬랙Id = "U00001";
    private static final String 써머_닉네임 = "써머";

    public static Member 써머() {
        return new Member(써머_슬랙Id, 써머_닉네임, String.format("https://%s.png", 써머_닉네임));
    }
}
