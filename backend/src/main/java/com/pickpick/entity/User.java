package com.pickpick.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Getter
@Table(name ="users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slack_id", length = 15, nullable = false, unique = true)
    private String slackId;

    @Column(name = "username", length = 80, nullable = false)
    private String username;

    @Column(name = "thumbnail", length = 2048, nullable = false)
    private String thumbnail;

    protected User() {
    }

    public User(final String slackId, final String username, final String thumbnail) {
        this.slackId = slackId;
        this.username = username;
        this.thumbnail = thumbnail;
    }
}

