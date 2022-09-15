package com.pickpick.slackevent.application.member;

import com.pickpick.exception.member.MemberNotFoundException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.member.dto.MemberProfileChangedDto;
import com.pickpick.slackevent.application.member.dto.MemberRequest;
import com.pickpick.slackevent.application.member.dto.ProfileDto;
import com.pickpick.utils.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional
@Service
public class MemberChangedService implements SlackEventService {

    private final MemberRepository members;

    public MemberChangedService(final MemberRepository members) {
        this.members = members;
    }

    @Override
    public void execute(final String requestBody) {
        MemberProfileChangedDto memberProfileChangedDto = convert(requestBody);

        String slackId = memberProfileChangedDto.getSlackId();
        Member member = members.findBySlackId(slackId)
                .orElseThrow(() -> new MemberNotFoundException(slackId));

        member.update(memberProfileChangedDto.getUsername(), memberProfileChangedDto.getThumbnailUrl());
    }

    private MemberProfileChangedDto convert(final String requestBody) {
        MemberRequest request = JsonUtils.convert(requestBody, MemberRequest.class);
        ProfileDto profile = request.getEvent().getUser().getProfile();

        return new MemberProfileChangedDto(
                request.getEvent().getUser().getId(),
                extractUsername(profile),
                profile.getImage512()
        );
    }

    private String extractUsername(final ProfileDto profile) {
        String username = profile.getDisplayName();

        if (!StringUtils.hasText(username)) {
            return profile.getRealName();
        }

        return username;
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MEMBER_CHANGED == slackEvent;
    }
}
