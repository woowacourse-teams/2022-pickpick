package com.pickpick.slackevent.application.member.dto;

import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class MemberRequest {

    private MemberEventDto event;

    private MemberRequest() {
    }

    public MemberRequest(final MemberEventDto event) {
        this.event = event;
    }

    public MemberProfileChangedDto toMemberProfileChangedDto() {
        ProfileDto profile = event.getUser().getProfile();

        return new MemberProfileChangedDto(
                event.getUser().getId(),
                extractUsername(profile),
                profile.getImage48()
        );
    }

    public MemberJoinDto toMemberJoinDto() {
        ProfileDto profile = event.getUser().getProfile();

        return MemberJoinDto.builder()
                .slackId(event.getUser().getId())
                .username(extractUsername(profile))
                .thumbnailUrl(profile.getImage48())
                .build();
    }

    private String extractUsername(final ProfileDto profile) {
        String username = profile.getDisplayName();

        if (!StringUtils.hasText(username)) {
            return profile.getRealName();
        }

        return username;
    }
}
