package com.pickpick.support;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.support.SlackIdExtractor;
import com.pickpick.message.ui.dto.BookmarkResponse;
import com.pickpick.message.ui.dto.MessageResponse;
import com.pickpick.message.ui.dto.MessageResponses;
import com.pickpick.message.ui.dto.ReminderResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Aspect
@Component
@Slf4j
public class MentionAspect<T> {

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
            value = "mentionTarget() && args(memberId, ..)",
            returning = "results",
            argNames = "joinPoint, memberId, results")
    public void replaceMention(final JoinPoint joinPoint, final Long memberId, final Object results) {
        Map<String, String> memberNames = extractMemberNames(memberId);

        MessageResponses responses = (MessageResponses) results;
        List<MessageResponse> responseList = responses.getMessages();

        //setFields(results, memberNames);
        replaceMessageMentionMembers(memberId, responseList);
    }

    private void setFields(final Object results, final Map<String, String> memberNames) {
        ReflectionUtils.doWithFields(results.getClass(), field -> {
            field.setAccessible(true);
            if ("text".equals(field.getName())) {
                replaceMentionMemberInText("hi", memberNames);
            }
        });
    }

    private Map<String, String> extractMemberNames(final Long memberId) {
        Member member = members.getById(memberId);
        List<Member> workspaceMembers = members.findAllByWorkspace(member.getWorkspace());

        Map<String, String> memberNames = workspaceMembers.stream()
                .collect(Collectors.toMap(Member::getSlackId,
                        workspaceMember -> MENTION_MARK + workspaceMember.getUsername()));
        return memberNames;
    }

    private void replaceMessageMembers(final Long memberId, final List<T> messageResponses) {
        Member member = members.getById(memberId);
        List<Member> workspaceMembers = members.findAllByWorkspace(member.getWorkspace());

        Map<String, String> memberNames = workspaceMembers.stream()
                .collect(Collectors.toMap(Member::getSlackId,
                        workspaceMember -> MENTION_MARK + workspaceMember.getUsername()));

        for (T message : messageResponses) {
//            String text = replaceMentionMemberInText(message.getText(), memberNames);
//            message.replaceText(text);
        }
    }


    private void replaceMessageMentionMembers(final Long memberId, final List<MessageResponse> messageResponses) {
        Member member = members.getById(memberId);
        List<Member> workspaceMembers = members.findAllByWorkspace(member.getWorkspace());

        Map<String, String> memberNames = workspaceMembers.stream()
                .collect(Collectors.toMap(Member::getSlackId,
                        workspaceMember -> MENTION_MARK + workspaceMember.getUsername()));

        for (MessageResponse message : messageResponses) {
            String text = replaceMentionMemberInText(message.getText(), memberNames);
            message.replaceText(text);
        }
    }

    private void replaceBookmarkMentionMembers(final Long memberId, final List<BookmarkResponse> bookmarkResponses) {
        Member member = members.getById(memberId);
        List<Member> workspaceMembers = members.findAllByWorkspace(member.getWorkspace());

        Map<String, String> memberNames = workspaceMembers.stream()
                .collect(Collectors.toMap(Member::getSlackId,
                        workspaceMember -> MENTION_MARK + workspaceMember.getUsername()));

        for (BookmarkResponse response : bookmarkResponses) {
            String text = replaceMentionMemberInText(response.getText(), memberNames);
            response.replaceText(text);
        }
    }

    private void replaceReminderMentionMembers(final Long memberId, final List<ReminderResponse> reminderResponses) {
        Member member = members.getById(memberId);
        List<Member> workspaceMembers = members.findAllByWorkspace(member.getWorkspace());

        Map<String, String> memberNames = workspaceMembers.stream()
                .collect(Collectors.toMap(Member::getSlackId,
                        workspaceMember -> MENTION_MARK + workspaceMember.getUsername()));

        for (ReminderResponse response : reminderResponses) {
            String text = replaceMentionMemberInText(response.getText(), memberNames);
            response.replaceText(text);
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
