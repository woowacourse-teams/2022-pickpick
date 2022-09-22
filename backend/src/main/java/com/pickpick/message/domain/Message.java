package com.pickpick.message.domain;

import com.pickpick.channel.domain.Channel;
import com.pickpick.member.domain.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;

@Getter
@Table(name = "message")
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slack_message_id", length = 50, nullable = false, updatable = false)
    private String slackId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Column(name = "text", length = 12000, nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "posted_date", nullable = false, columnDefinition = "timestamp(6)")
    private LocalDateTime postedDate;

    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    protected Message() {
    }

    public Message(final String slackId, final String text, final Member member, final Channel channel,
                   final LocalDateTime postedDate, final LocalDateTime modifiedDate) {
        this.slackId = slackId;
        this.text = text;
        this.member = member;
        this.channel = channel;
        this.postedDate = postedDate;
        this.modifiedDate = modifiedDate;
    }

    public void changeText(final String text, final LocalDateTime modifiedDate) {
        this.text = text;
        this.modifiedDate = modifiedDate;
    }
}
