package com.pickpick.message.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.ui.dto.BookmarkRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookmarkServiceTest {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private MemberRepository members;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private ChannelRepository channels;

    @DisplayName("북마크를 생성한다")
    @Test
    void save() {
        // given
        Member member = new Member("U1234", "사용자", "user.png");
        members.save(member);
        Channel channel = new Channel("C1234", "기본채널");
        channels.save(channel);
        Message message = new Message("M1234", "메시지", member, channel, LocalDateTime.now(), LocalDateTime.now());
        messages.save(message);

        // when
        assertDoesNotThrow(() -> bookmarkService.save(member.getId(), new BookmarkRequest(message.getId())));
    }
}
