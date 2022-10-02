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

    protected ErrorCodeSnippet(final String snippetName, final String templateName) {
        super(snippetName, templateName, null);
    }

    private static Class<?> apply(BeanDefinition component) {
        try {
            return Class.forName(component.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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

    public void addErrorCodes(final List<Map<String, Object>> fields) {
        Set<? extends Class<?>> exceptionClasses = findExceptionClasses();
        for (Class<?> aClass : exceptionClasses) {
            Map<String, Object> model = new HashMap<>();
            try {
                Field errorCodeField = aClass.getDeclaredField("ERROR_CODE");
                Field clientMessageField = aClass.getDeclaredField("CLIENT_MESSAGE");
                errorCodeField.setAccessible(true);
                clientMessageField.setAccessible(true);
                String errorCode = errorCodeField.get(null).toString();
                String errorMessage = clientMessageField.get(null).toString();
                System.out.println(errorCode);
                System.out.println(errorMessage);
                model.put("path", errorCode);
                model.put("description", errorMessage);
                fields.add(model);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                // ignored
            }
        }
    }

    public Set<? extends Class<?>> findExceptionClasses() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(BadRequestException.class));
        provider.addIncludeFilter(new AssignableTypeFilter(NotFoundException.class));

        Set<BeanDefinition> components = provider.findCandidateComponents("com.pickpick.exception");
        return components.stream()
                .map(ErrorCodeSnippet::apply)
                .collect(Collectors.toSet());
    }
}
