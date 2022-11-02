package com.pickpick.support;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.support.SlackIdExtractor;
import java.lang.reflect.Field;
import java.util.Arrays;
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
            returning = "returnValue",
            argNames = "memberId, returnValue")
    public void replaceMention(final Long memberId, final Object returnValue) throws IllegalAccessException {
        Map<String, String> memberNames = extractMemberNames(memberId);
        List<Object> responses = getObjectContainingText(returnValue);

        for (Object response : responses) {
            List<Field> texts = filterTextFields(response);
            replaceMentionMemberInText(texts, response, memberNames);
        }
    }

    private Map<String, String> extractMemberNames(final Long memberId) {
        Member member = members.getById(memberId);
        List<Member> workspaceMembers = members.findAllByWorkspace(member.getWorkspace());

        return workspaceMembers.stream()
                .collect(Collectors.toMap(Member::getSlackId,
                        workspaceMember -> MENTION_MARK + workspaceMember.getUsername()));
    }

    private List<Object> getObjectContainingText(final Object returnValue) {
        return Arrays.stream(returnValue.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .filter(field -> field.getType().equals(List.class))
                .map(FunctionWrapper.apply((field) -> (List<Object>) field.get(returnValue)))
                .findFirst()
                .orElse(List.of(returnValue));
    }

    private List<Field> filterTextFields(final Object response) {
        return Arrays.stream(response.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .filter(field -> field.getName().equals("text"))
                .collect(Collectors.toList());
    }

    private void replaceMentionMemberInText(List<Field> fields, Object results, Map<String, String> memberMap)
            throws IllegalAccessException {

        for (Field field : fields) {
            String text = (String) field.get(results);
            text = getReplacedText(memberMap, text);
            field.set(results, text);
        }
    }

    private String getReplacedText(final Map<String, String> memberMap, String text) {
        Set<String> slackIds = slackIdExtractor.extract(text);
        for (String slackId : slackIds) {
            String mention = MENTION_PREFIX + slackId + MENTION_SUFFIX;
            text = text.replace(mention, memberMap.getOrDefault(slackId, mention));
        }
        return text;
    }
}
