package com.pickpick.member.domain;

import com.pickpick.exception.MemberInvalidThumbnailUrlException;
import com.pickpick.exception.MemberInvalidUsernameException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Table(name = "member")
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slack_id", length = 15, nullable = false, unique = true)
    private String slackId;

    @Column(name = "username", length = 80, nullable = false)
    private String username;

    @Column(name = "thumbnail_url", length = 2048, nullable = false)
    private String thumbnailUrl;

    @Column(name = "first_login", nullable = false)
    private boolean isFirstLogin = true;

    protected Member() {
    }

    public Member(final String slackId, final String username, final String thumbnailUrl) {
        this.slackId = slackId;
        this.username = username;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void markLoggedIn() {
        this.isFirstLogin = false;
    }

    public void update(final String username, final String thumbnailUrl) {
        validateUsername(username);
        validateThumbnailUrl(thumbnailUrl);

        this.username = username;
        this.thumbnailUrl = thumbnailUrl;
    }

    private void validateUsername(final String username) {
        if (!StringUtils.hasText(username)) {
            throw new MemberInvalidUsernameException(username);
        }
    }

    private void validateThumbnailUrl(final String thumbnailUrl) {
        if (!StringUtils.hasText(thumbnailUrl)) {
            throw new MemberInvalidThumbnailUrlException(thumbnailUrl);
        }
    }
}
