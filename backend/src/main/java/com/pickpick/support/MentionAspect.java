package com.pickpick.support;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.support.SlackIdExtractor;
import com.pickpick.message.ui.dto.MessageTextResponse;
import com.pickpick.message.ui.dto.MessageTextResponses;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MentionAspect {

    private static final String MENTION_PREFIX = "<@";
    private static final String MENTION_SUFFIX = ">";
    private static final String MENTION_MARK = "@";

    private final MemberRepository members;
    private final SlackIdExtractor slackIdExtractor;

    public MentionAspect(final MemberRepository members, final SlackIdExtractor slackIdExtractor) {
        this.members = members;
        this.slackIdExtractor = slackIdExtractor;
    }

    @Pointcut("@annotation(com.pickpick.support.MentionIdReplaceable)")
    private void mentionTarget() {

    }

    @AfterReturning(
            value = "mentionTarget() && (args(memberId, ..) || args(.., memberId))",
            returning = "results",
            argNames = "memberId, results")
    public void replaceMention(final Long memberId,
                               final MessageTextResponses<MessageTextResponse> results) {
        List<MessageTextResponse> responseList = results.findContents();
        Map<String, String> memberNames = extractMemberNames(memberId);

        replaceMessageMembers(responseList, memberNames);
    }

    @AfterReturning(
            value = "mentionTarget() && (args(memberId, ..) || args(.., memberId))",
            returning = "results",
            argNames = "memberId, results")
    public void replaceMention(final Long memberId,
                               final MessageTextResponse results) {
        Map<String, String> memberNames = extractMemberNames(memberId);

        replaceMessageMembers(List.of(results), memberNames);
    }

    private Map<String, String> extractMemberNames(final Long memberId) {
        Member member = members.getById(memberId);
        List<Member> workspaceMembers = members.findAllByWorkspace(member.getWorkspace());

        return workspaceMembers.stream()
                .collect(Collectors.toMap(Member::getSlackId,
                        workspaceMember -> MENTION_MARK + workspaceMember.getUsername()));
    }

    private void replaceMessageMembers(final List<MessageTextResponse> messageResponses,
                                       final Map<String, String> memberNames) {
        for (MessageTextResponse message : messageResponses) {
            String text = replaceMentionMemberInText(message.getText(), memberNames);
            message.replaceText(text);
        }
    }

    private String replaceMentionMemberInText(String text, final Map<String, String> memberMap) {
        Set<String> slackIds = slackIdExtractor.extract(text);
        for (String slackId : slackIds) {
            String mention = MENTION_PREFIX + slackId + MENTION_SUFFIX;
            text = text.replace(mention, memberMap.getOrDefault(slackId, mention));
        }
        return text;
    }
}
