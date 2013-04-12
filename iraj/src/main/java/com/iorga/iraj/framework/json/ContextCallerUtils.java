package com.iorga.iraj.framework.json;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;

import com.iorga.iraj.framework.annotation.ContextPath;

public class ContextCallerUtils {
	public static <T extends AnnotatedElement & Member> String getContextPath(final T targetAnnotatedMember, final PropertyTemplate<T> propertyTemplate) {
		final ContextPath contextPathAnnotation = targetAnnotatedMember.getAnnotation(ContextPath.class);
		if (contextPathAnnotation != null) {
			return contextPathAnnotation.value();
		} else {
			return propertyTemplate.getPropertyName(targetAnnotatedMember);
		}
	}
}
