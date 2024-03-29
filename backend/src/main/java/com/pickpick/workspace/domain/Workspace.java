package com.pickpick.workspace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Getter
@Table(name = "workspace")
@Entity
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slack_id", length = 15, nullable = false, unique = true, updatable = false)
    private String slackId;

    @Column(name = "bot_token", length = 256, nullable = false, unique = true, updatable = false)
    private String botToken;

    @Column(name = "bot_slack_id", length = 15, nullable = false, unique = true, updatable = false)
    private String botSlackId;

    protected Workspace() {
    }

    public Workspace(final String slackId, final String botToken, final String botSlackId) {
        this.slackId = slackId;
        this.botToken = botToken;
        this.botSlackId = botSlackId;
    }
}
