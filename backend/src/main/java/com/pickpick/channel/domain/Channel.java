package com.pickpick.channel.domain;

import com.pickpick.exception.channel.ChannelInvalidNameException;
import com.pickpick.workspace.domain.Workspace;
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
import org.springframework.util.StringUtils;

@Getter
@Table(name = "channel")
@Entity
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slack_id", length = 15, nullable = false, unique = true, updatable = false)
    private String slackId;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    protected Channel() {
    }

    public Channel(final String slackId, final String name) {
        this.slackId = slackId;
        this.name = name;
    }

    public Channel(final String slackId, final String name, final Workspace workspace) {
        this.slackId = slackId;
        this.name = name;
        this.workspace = workspace;
    }

    public void changeName(final String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new ChannelInvalidNameException(name);
        }
    }
}
