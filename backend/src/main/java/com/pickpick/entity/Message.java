package com.pickpick.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "posted_date")
    private LocalDateTime postedDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    protected Message() {
    }

    public Message(final String text, final User user, final LocalDateTime postedDate,
                   final LocalDateTime modifiedDate) {
        this.text = text;
        this.user = user;
        this.postedDate = postedDate;
        this.modifiedDate = modifiedDate;
    }
}
