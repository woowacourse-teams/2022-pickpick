package com.pickpick.slackevent.application.member;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventHandler;
import com.pickpick.slackevent.application.member.dto.MemberProfileChangedDto;
import com.pickpick.slackevent.application.member.dto.MemberRequest;
import com.pickpick.utils.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberChangedEventHandler implements SlackEventHandler {

    private final MemberRepository members;

    public MemberChangedEventHandler(final MemberRepository members) {
        this.members = members;
    }

    @Override
    public void execute(final String requestBody) {
        MemberProfileChangedDto memberProfileChangedDto = convert(requestBody);

        String slackId = memberProfileChangedDto.getSlackId();
        Member member = members.getBySlackId(slackId);

        member.update(memberProfileChangedDto.getUsername(), memberProfileChangedDto.getThumbnailUrl());
    }

    private MemberProfileChangedDto convert(final String requestBody) {
        MemberRequest request = JsonUtils.convert(requestBody, MemberRequest.class);
        return request.toMemberProfileChangedDto();
    }

    @Override
    public SlackEvent getSlackEvent() {
        return SlackEvent.MEMBER_CHANGED;
    }
}
