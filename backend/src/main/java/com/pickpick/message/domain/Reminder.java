package com.pickpick.message.domain;

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
@Table(name = "reminder")
@Entity
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(name = "remind_date", nullable = false)
    private LocalDateTime remindDate;

    protected Reminder() {
    }

    public Reminder(final Member member, final Message message, final LocalDateTime remindDate) {
        this.member = member;
        this.message = message;
        this.remindDate = remindDate;
    }

    public void updateRemindDate(final LocalDateTime remindDate) {
        this.remindDate = remindDate;
    }
}
