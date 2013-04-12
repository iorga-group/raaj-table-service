package com.iorga.iraj.framework.json;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.iorga.iraj.framework.annotation.ContextParam;
import com.iorga.iraj.framework.annotation.ContextParams;

public class MapContextCaller<T extends AnnotatedElement & Member> implements ContextCaller {
	private final String contextKey;
	private final ContextCaller nextContextCaller;
	private final Type sourceType;

	public MapContextCaller(final T targetAnnotatedMember, final ContextParams contextParams, final PropertyTemplate<T> propertyTemplate) {
		// First we have to know what is the first context path part of the target annotated member
		final String contextPath = ContextCallerUtils.getContextPath(targetAnnotatedMember, propertyTemplate);

		final String firstContextPathPart = StringUtils.substringBefore(contextPath, ".");

		// Now search the @ContextParam corresponding to the targetAnnotatedMember
		ContextCaller nextContextCaller = null;
		Type sourceType = null;
		for (final ContextParam contextParam : contextParams.value()) {
			String contextName = contextParam.name();
			final Class<?> contextClass = contextParam.value();
			if (StringUtils.isBlank(contextName)) {
				contextName = StringUtils.uncapitalize(contextClass.getSimpleName());
			}
			if (firstContextPathPart.equals(contextName)) {
				// we have our @ContextParam now let's try to know if there is a "nextContextCaller"
				if (firstContextPathPart.equals(contextPath)) {
					// finished to handle the path, there is no other caller
					nextContextCaller = null;
					sourceType = contextParam.value(); // TODO gérer les listes paramétrées
				} else {
					nextContextCaller = new ObjectContextCaller(contextClass, StringUtils.substringAfter(contextPath, "."));
					sourceType = nextContextCaller.getSourceType();
				}
				break;
			}
		}
		if (sourceType == null) {
			throw new IllegalArgumentException("Couln't find the @ContextParam corresponding to "+firstContextPathPart+" in "+targetAnnotatedMember.getDeclaringClass()+" for path "+contextPath);
		} else {
			this.nextContextCaller = nextContextCaller;
			this.sourceType = sourceType;
			this.contextKey = firstContextPathPart;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object callContext(final Object context) {
		final Object mappedObject = ((Map<String, Object>)context).get(contextKey);
		if (nextContextCaller != null && mappedObject != null) {
			return nextContextCaller.callContext(mappedObject);
		} else {
			return mappedObject;
		}
	}

	@Override
	public Type getSourceType() {
		return sourceType;
	}

}
