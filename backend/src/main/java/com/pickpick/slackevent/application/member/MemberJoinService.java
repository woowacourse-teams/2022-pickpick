package com.pickpick.slackevent.application.member;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.member.dto.MemberJoinDto;
import com.pickpick.slackevent.application.member.dto.MemberRequest;
import com.pickpick.slackevent.application.member.dto.ProfileDto;
import com.pickpick.utils.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional
@Service
public class MemberJoinService implements SlackEventService {

    private final MemberRepository members;

    public MemberJoinService(final MemberRepository members) {
        this.members = members;
    }

    @Override
    public void execute(final String requestBody) {
        MemberJoinDto memberJoinDto = convert(requestBody);
        Member newMember = toMember(memberJoinDto);

        members.save(newMember);
    }

    private MemberJoinDto convert(final String requestBody) {
        MemberRequest request = JsonUtils.convert(requestBody, MemberRequest.class);
        ProfileDto profile = request.getEvent().getUser().getProfile();

        return MemberJoinDto.builder()
                .slackId(request.getEvent().getUser().getId())
                .username(extractUsername(profile))
                .thumbnailUrl(profile.getImage512())
                .build();
    }

    private String extractUsername(final ProfileDto profile) {
        String username = profile.getDisplayName();

        if (!StringUtils.hasText(username)) {
            return profile.getRealName();
        }

        return username;
    }

    private Member toMember(final MemberJoinDto memberJoinDto) {
        return new Member(
                memberJoinDto.getSlackId(),
                memberJoinDto.getUsername(),
                memberJoinDto.getThumbnailUrl()
        );
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MEMBER_JOIN == slackEvent;
    }
}
