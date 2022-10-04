package com.pickpick.support;

import com.pickpick.exception.BadRequestException;
import com.pickpick.exception.NotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;

public class ErrorCodeSnippet extends TemplatedSnippet {

    private static final String EXCEPTION_PACKAGE = "com.pickpick.exception";
    private static final String RUNTIME_EXCEPTION = "RuntimeException";

    protected ErrorCodeSnippet(final String snippetName, final String templateName) {
        super(snippetName, templateName, null);
    }

    private static Class<?> apply(BeanDefinition component) {
        try {
            return Class.forName(component.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected Map<String, Object> createModel(final Operation operation) {
        Map<String, Object> model = new HashMap<>();
        List<Map<String, Object>> fields = new ArrayList<>();
        model.put("fields", fields);
        addErrorCodes(fields);

        return model;
    }

    private void addErrorCodes(final List<Map<String, Object>> fields) {
        Set<? extends Class<?>> exceptionClasses = findExceptionClasses();
        for (Class<?> aClass : exceptionClasses) {
            Map<String, Object> model = new HashMap<>();
            addErrorCode(fields, aClass, model);
        }
    }

    private Set<? extends Class<?>> findExceptionClasses() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(BadRequestException.class));
        provider.addIncludeFilter(new AssignableTypeFilter(NotFoundException.class));

        Set<BeanDefinition> components = provider.findCandidateComponents(EXCEPTION_PACKAGE);
        return components.stream()
                .map(ErrorCodeSnippet::apply)
                .filter(this::isCustomException)
                .collect(Collectors.toSet());
    }

    private boolean isCustomException(final Class<?> aClass) {
        return !aClass.getSuperclass().getSimpleName().equals(RUNTIME_EXCEPTION);
    }

    private void addErrorCode(final List<Map<String, Object>> fields, final Class<?> aClass,
                              final Map<String, Object> model) {
        try {
            String errorCode = extractFieldValue(aClass, "ERROR_CODE");
            String errorMessage = extractFieldValue(aClass, "CLIENT_MESSAGE");
            model.put("errorCode", errorCode);
            model.put("errorMessage", errorMessage);
            fields.add(model);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("커스텀 예외 클래스 내부에 ErrorCode 및 ClientMessage 를 정의해야합니다.");
        } catch (IllegalAccessException ignored) {
        }
    }

    private String extractFieldValue(final Class<?> aClass, final String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field errorCodeField = aClass.getDeclaredField(fieldName);
        errorCodeField.setAccessible(true);
        return errorCodeField.get(null).toString();
    }
}
