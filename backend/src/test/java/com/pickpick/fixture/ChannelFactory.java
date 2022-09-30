package com.pickpick.fixture;

import com.pickpick.channel.domain.Channel;

public class ChannelFactory {

    public static Channel notice() {
        return new Channel("C0000000001", "공지사항");
    }

    public static Channel freeChat() {
        return new Channel("C0000000002", "잡담");
    }

    public static Channel qna() {
        return new Channel("C0000000003", "질문답변");
    }
}
