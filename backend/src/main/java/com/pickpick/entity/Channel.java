package com.pickpick.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(name = "channel")
@Entity
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slack_id", length = 15, nullable = false, unique = true)
    private String slackId;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    protected Channel() {
    }

    public Channel(final String slackId, final String name) {
        this.slackId = slackId;
        this.name = name;
    }
}
