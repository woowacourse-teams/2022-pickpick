package com.pickpick.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(name ="users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slack_id")
    private String slackId;

    @Column(name = "username")
    private String username;

    @Column(name = "thumbnail")
    private String thumbnail;

    protected User() {
    }

    public User(final String slackId, final String username, final String thumbnail) {
        this.slackId = slackId;
        this.username = username;
        this.thumbnail = thumbnail;
    }
}
