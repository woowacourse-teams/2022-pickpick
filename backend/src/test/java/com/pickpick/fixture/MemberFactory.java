package com.pickpick.fixture;

import com.pickpick.member.domain.Member;

public class MemberFactory {

    public static Member summer() {
        return new Member("U0000000001", "써머", "https://summer.png");
    }

    public static Member bom() {
        return new Member("U0000000002", "봄", "https://bom.png");
    }

    public static Member yeonlog() {
        return new Member("U0000000003", "연로그", "https://yeonlog.png");
    }

    public static Member hope() {
        return new Member("U0000000004", "호프", "https://hope.png");
    }

    public static Member kkojae() {
        return new Member("U0000000001", "꼬재", "https://kkojae.png");
    }
}
