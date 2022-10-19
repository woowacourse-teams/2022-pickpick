package com.pickpick.fixture;

import static com.pickpick.fixture.MemberFixture.YEONLOG;

public enum ParticipationFixture {
    YEONLOG_IN_ALL_CHANNELS(YEONLOG);

    private MemberFixture memberFixture;

    ParticipationFixture(final MemberFixture member) {
    }
}
