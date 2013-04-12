package com.iorga.iraj.framework.json;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;

import com.iorga.iraj.framework.annotation.ContextParam;

public class ObjectContextCaller implements ContextCaller {
	private Method getter;
	private final ContextCaller nextContextCaller;

	public <T extends AnnotatedElement & Member> ObjectContextCaller(final T targetAnnotatedMember, final ContextParam contextParam, final PropertyTemplate<T> propertyTemplate) {
		this(contextParam.value(), ContextCallerUtils.getContextPath(targetAnnotatedMember, propertyTemplate));
	}

	public ObjectContextCaller(final Class<?> currentClass, final String currentContextPath) throws SecurityException {
		final String firstContextPathPart = StringUtils.substringBefore(currentContextPath, ".");

		try {
			getter = currentClass.getMethod("get"+StringUtils.capitalize(firstContextPathPart));
		} catch (final NoSuchMethodException e) {
			try {
				getter = currentClass.getMethod("is"+StringUtils.capitalize(firstContextPathPart));
			} catch (final NoSuchMethodException e1) {
				throw new IllegalArgumentException("No getter for "+firstContextPathPart+" on "+currentClass);
			}
		}

		if (!firstContextPathPart.equals(currentContextPath)) {
			// the path is not finished to handle
			nextContextCaller = new ObjectContextCaller(getter.getReturnType(), StringUtils.substringAfter(currentContextPath, "."));
		} else {
			nextContextCaller = null;
		}
	}

	@Override
	public Object callContext(final Object context) {
		try {
			final Object nextContext = getter.invoke(context);
			if (nextContext != null && nextContextCaller != null) {
				return nextContextCaller.callContext(nextContext);
			} else {
				return nextContext;
			}
		} catch (final Exception e) {
			throw new IllegalStateException("Couldn't call "+getter);
		}
	}

	@Override
	public Type getSourceType() {
		return nextContextCaller != null ? nextContextCaller.getSourceType() : getter.getGenericReturnType();
	}

}
