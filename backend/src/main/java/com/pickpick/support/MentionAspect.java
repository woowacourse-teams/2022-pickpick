package com.pickpick.support;

import com.pickpick.exception.utils.ReflectionFailureException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.utils.SlackIdExtractUtils;
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
    private static final String TEXT = "text";

    private final MemberRepository members;

    public MentionAspect(final MemberRepository members) {
        this.members = members;
    }

    @Pointcut("@annotation(com.pickpick.support.MentionIdReplaceable)")
    private void mentionTarget() {
    }

    @AfterReturning(
            value = "mentionTarget() && (args(memberId, ..) || args(.., memberId))",
            returning = "returnValue",
            argNames = "memberId,returnValue")
    public void apply(final Long memberId, final Object returnValue) {
        try {
            replaceMention(memberId, returnValue);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void replaceMention(final Long memberId, final Object returnValue) {
        Map<String, String> memberNames = extractMemberNames(memberId);
        List<Object> responses = getObjectContainingText(returnValue);

        for (Object response : responses) {
            List<Field> texts = filterTextFields(response);
            replaceMentionIdInText(texts, response, memberNames);
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
                .filter(field -> field.getType().equals(List.class))
                .map(field -> getValues(returnValue, field))
                .findFirst()
                .orElse(List.of(returnValue));
    }

    private List<Object> getValues(final Object returnValue, final Field field) {
        try {
            field.setAccessible(true);
            return (List<Object>) field.get(returnValue);
        } catch (IllegalAccessException e) {
            throw new ReflectionFailureException();
        }
    }

    private List<Field> filterTextFields(final Object response) {
        return Arrays.stream(response.getClass().getDeclaredFields())
                .filter(field -> field.getName().equals(TEXT))
                .collect(Collectors.toList());
    }

    private void replaceMentionIdInText(final List<Field> fields, final Object results,
                                        final Map<String, String> memberMap) {
        for (Field field : fields) {
            field.setAccessible(true);
            replaceField(results, memberMap, field);
        }
    }

    private void replaceField(final Object results, final Map<String, String> memberMap, final Field field) {
        try {
            String text = (String) field.get(results);
            text = getReplacedText(memberMap, text);
            field.set(results, text);
        } catch (IllegalAccessException e) {
            throw new ReflectionFailureException();
        }
    }

    private String getReplacedText(final Map<String, String> memberMap, String text) {
        Set<String> slackIds = SlackIdExtractUtils.extract(text);
        for (String slackId : slackIds) {
            String mention = MENTION_PREFIX + slackId + MENTION_SUFFIX;
            text = text.replace(mention, memberMap.getOrDefault(slackId, mention));
        }
        return text;
    }
}
