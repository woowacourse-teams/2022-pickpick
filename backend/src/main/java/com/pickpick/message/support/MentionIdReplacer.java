package com.pickpick.message.support;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class MentionIdReplacer {

    private static final String MENTION_PREFIX = "<@";
    private static final String MENTION_SUFFIX = ">";
    private static final String MENTION_MARK = "@";

    private final MemberRepository members;
    private final SlackIdExtractor slackIdExtractor;

    public MentionIdReplacer(final MemberRepository members, final SlackIdExtractor slackIdExtractor) {
        this.members = members;
        this.slackIdExtractor = slackIdExtractor;
    }

    @NotNull
    public Map<String, String> extractMemberNames(final Long memberId) {
        Member member = members.getById(memberId);
        List<Member> workspaceMembers = members.findAllByWorkspace(member.getWorkspace());

        Map<String, String> memberNames = workspaceMembers.stream()
                .collect(Collectors.toMap(Member::getSlackId,
                        workspaceMember -> MENTION_MARK + workspaceMember.getUsername()));
        return memberNames;
    }

    public String replaceMentionMemberInText(String text, final Map<String, String> memberMap) {
        Set<String> slackIds = slackIdExtractor.extract(text);
        for (String slackId : slackIds) {
            String mention = MENTION_PREFIX + slackId + MENTION_SUFFIX;
            text = text.replace(mention, memberMap.getOrDefault(slackId, mention));
        }
        return text;
    }
}
