package com.pickpick.slackevent.application.member;

import com.pickpick.exception.MemberNotFoundException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.member.dto.MemberProfileDto;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberChangedService implements SlackEventService {

    public static final String USER = "user";
    public static final String PROFILE = "profile";
    public static final String SLACK_ID = "id";
    public static final String DISPLAY_NAME = "display_name";
    public static final String IMAGE_URL = "image_512";

    private final MemberRepository members;

    public MemberChangedService(final MemberRepository members) {
        this.members = members;
    }

    @Override
    public void execute(final Map<String, Object> requestBody) {
        MemberProfileDto memberProfileDto = convert(requestBody);

        String slackId = memberProfileDto.getSlackId();
        Member member = members.findBySlackId(slackId)
                .orElseThrow(() -> new MemberNotFoundException(slackId));

        member.update(memberProfileDto.getUsername(), memberProfileDto.getThumbnailUrl());
    }

    private MemberProfileDto convert(final Map<String, Object> requestBody) {
        Map<String, Object> user = (Map) requestBody.get(USER);
        Map<String, Object> profile = (Map) user.get(PROFILE);

        return new MemberProfileDto(
                (String) user.get(SLACK_ID),
                (String) profile.get(DISPLAY_NAME),
                (String) profile.get(IMAGE_URL)
        );
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return false;
    }
}
