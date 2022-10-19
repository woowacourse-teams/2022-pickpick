package com.pickpick.fixture;

import static com.pickpick.fixture.MemberFixture.BOM;
import static com.pickpick.fixture.MemberFixture.SUMMER;

import com.pickpick.channel.domain.Channel;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Message;
import java.time.LocalDateTime;
import java.util.UUID;

public enum MessageFixtures {

    PLAIN_20220712_14_00_00("일반 텍스트", LocalDateTime.of(2022, 7, 12, 14, 0, 0)),
    PLAIN_20220712_15_00_00("일반 텍스트", LocalDateTime.of(2022, 7, 12, 15, 0, 0)),
    PLAIN_20220712_16_00_00("일반 텍스트", LocalDateTime.of(2022, 7, 12, 16, 0, 0)),
    PLAIN_20220712_17_00_00("일반 텍스트", LocalDateTime.of(2022, 7, 12, 17, 0, 0)),
    PLAIN_20220712_18_00_00("일반 텍스트", LocalDateTime.of(2022, 7, 12, 18, 0, 0)),

    EMPTY_20220713_14_00_00("", LocalDateTime.of(2022, 7, 13, 14, 0, 0)),
    EMPTY_20220713_15_00_00("", LocalDateTime.of(2022, 7, 13, 15, 0, 0)),
    EMPTY_20220713_16_00_00("", LocalDateTime.of(2022, 7, 13, 16, 0, 0)),
    EMPTY_20220713_17_00_00("", LocalDateTime.of(2022, 7, 13, 17, 0, 0)),
    EMPTY_20220713_18_00_00("", LocalDateTime.of(2022, 7, 13, 18, 0, 0)),

    KEYWORD_20220714_14_00_00("줍줍 텍스트", LocalDateTime.of(2022, 7, 14, 14, 0, 0)),
    KEYWORD_20220714_15_00_00("줍줍 텍스트", LocalDateTime.of(2022, 7, 14, 15, 0, 0)),
    KEYWORD_20220714_16_00_00("줍줍 텍스트", LocalDateTime.of(2022, 7, 14, 16, 0, 0)),
    KEYWORD_20220714_17_00_00("줍줍 텍스트", LocalDateTime.of(2022, 7, 14, 17, 0, 0)),
    KEYWORD_20220714_18_00_00("줍줍 텍스트", LocalDateTime.of(2022, 7, 14, 18, 0, 0)),

    PLAIN_20220715_14_00_00("일반 텍스트", LocalDateTime.of(2022, 7, 15, 14, 0, 0)),
    PLAIN_20220715_15_00_00("일반 텍스트", LocalDateTime.of(2022, 7, 15, 15, 0, 0)),
    PLAIN_20220715_16_00_00("일반 텍스트", LocalDateTime.of(2022, 7, 15, 16, 0, 0)),
    PLAIN_20220715_17_00_00("일반 텍스트", LocalDateTime.of(2022, 7, 15, 17, 0, 0)),
    PLAIN_20220715_18_00_00("일반 텍스트", LocalDateTime.of(2022, 7, 15, 18, 0, 0)),

    KEYWORD_20220716_14_00_00("줍줍 텍스트", LocalDateTime.of(2022, 7, 16, 14, 0, 0)),
    KEYWORD_20220716_15_00_00("줍줍 텍스트", LocalDateTime.of(2022, 7, 16, 15, 0, 0)),
    KEYWORD_20220716_16_00_00("줍줍 텍스트", LocalDateTime.of(2022, 7, 16, 16, 0, 0)),
    KEYWORD_20220716_17_00_00("줍줍 텍스트", LocalDateTime.of(2022, 7, 16, 17, 0, 0)),
    KEYWORD_20220716_18_00_00("줍줍 텍스트", LocalDateTime.of(2022, 7, 16, 18, 0, 0)),

    MENTION_20220816_14_00_00(createMentionText(BOM), LocalDateTime.of(2022, 8, 16, 14, 0, 0)),
    MENTION_20220816_15_00_00("<@UNOTEXISTED> 존재하지 않는 유저 멘션 텍스트", LocalDateTime.of(2022, 8, 16, 15, 0, 0)),
    MENTION_20220816_16_00_00(createSeveralMentionText(BOM), LocalDateTime.of(2022, 8, 16, 16, 0, 0)),
    MENTION_20220816_17_00_00(createSeveralUserMentionText(), LocalDateTime.of(2022, 8, 16, 17, 0, 0)),
    ;

    private final String text;
    private final LocalDateTime dateTime;

    MessageFixtures(final String text, final LocalDateTime dateTime) {
        this.text = text;
        this.dateTime = dateTime;
    }

    public Message create(final Channel channel, final Member member) {
        return new Message(UUID.randomUUID().toString(), this.text, member, channel, this.dateTime, this.dateTime);
    }

    private static String createMentionText(final MemberFixture memberFixture) {
        return "<@" + memberFixture.getSlackId() + "> 한 번 존재하는 유저 멘션 텍스트";
    }

    private static String createSeveralMentionText(final MemberFixture memberFixture) {
        return "<@" + memberFixture.getSlackId() + "> 여러번 존재하는 유저 멘션 텍스트" + "<@" + memberFixture.getSlackId() + ">";
    }

    private static String createSeveralUserMentionText() {
        return "<@" + BOM.getSlackId() + ">" + "여러명 유저 멘션 텍스트" + "<@" + SUMMER.getSlackId() + ">";
    }
}
