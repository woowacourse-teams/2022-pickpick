package com.pickpick.slackevent.application.member;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.member.dto.MemberJoinDto;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional
@Service
public class MemberJoinService implements SlackEventService {

    private static final String USER = "user";
    private static final String PROFILE = "profile";
    private static final String SLACK_ID = "id";
    private static final String DISPLAY_NAME = "display_name";
    private static final String IMAGE_URL = "image_512";
    private static final String REAL_NAME = "real_name";
    private static final String EVENT = "event";

    private final MemberRepository members;

    public MemberJoinService(final MemberRepository members) {
        this.members = members;
    }

    @Override
    public void execute(final Map<String, Object> requestBody) {
        MemberJoinDto memberJoinDto = convert(requestBody);

        Member newMember = toMember(memberJoinDto);

        members.save(newMember);
    }

    private MemberJoinDto convert(final Map<String, Object> requestBody) {
        Map<String, Object> event = (Map) requestBody.get(EVENT);
        Map<String, Object> user = (Map) event.get(USER);
        Map<String, Object> profile = (Map) user.get(PROFILE);

        return MemberJoinDto.builder()
                .slackId((String) user.get(SLACK_ID))
                .username(extractUsername(profile))
                .thumbnailUrl((String) profile.get(IMAGE_URL))
                .build();
    }

    private String extractUsername(final Map<String, Object> profile) {
        String username = (String) profile.get(DISPLAY_NAME);

        if (!StringUtils.hasText(username)) {
            return (String) profile.get(REAL_NAME);
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
