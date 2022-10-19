package com.pickpick.fixture;

import com.pickpick.channel.domain.Channel;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Message;
import java.time.LocalDateTime;
import java.util.UUID;

public enum MessageFixture {

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
    ;

    private final String text;
    private final LocalDateTime dateTime;

    MessageFixture(final String text, final LocalDateTime dateTime) {
        this.text = text;
        this.dateTime = dateTime;
    }

    public Message create(final Channel channel, final Member member) {
        return new Message(UUID.randomUUID().toString(), this.text, member, channel, this.dateTime, this.dateTime);
    }
}
