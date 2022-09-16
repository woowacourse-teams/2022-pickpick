package com.pickpick.slackevent.application.member;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.member.dto.MemberJoinDto;
import com.pickpick.slackevent.application.member.dto.MemberRequest;
import com.pickpick.utils.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Member newMember = memberJoinDto.toEntity();

        members.save(newMember);
    }

    private MemberJoinDto convert(final String requestBody) {
        MemberRequest request = JsonUtils.convert(requestBody, MemberRequest.class);
        return request.toMemberJoinDto();
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MEMBER_JOIN == slackEvent;
    }
}
