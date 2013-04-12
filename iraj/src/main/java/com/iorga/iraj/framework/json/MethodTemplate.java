package com.iorga.iraj.framework.json;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang3.StringUtils;

import com.iorga.iraj.framework.annotation.ContextParam;
import com.iorga.iraj.framework.annotation.ContextPath;

public class MethodTemplate extends PropertyTemplate<Method> {
	private static final int PUBLIC_STATIC = Modifier.STATIC | Modifier.PUBLIC;
	protected final Template propertyTemplate;
	protected final Method targetMethod;

	public MethodTemplate(final Method targetMethod) {
		super(targetMethod);

		if ((targetMethod.getModifiers() & PUBLIC_STATIC) != PUBLIC_STATIC) {
			throw new IllegalArgumentException(targetMethod+" must be public static in order to be called without instantiating the template");
		}

		this.targetMethod = targetMethod;

		final Class<?> returnType = targetMethod.getReturnType();
		if (TemplateUtils.isTemplate(returnType)) {
			// We still have a template, let's continue
			propertyTemplate = new ClassTemplate(returnType);
		} else {
			// Let's write the returned object
			//TODO gérer les collections
			propertyTemplate = new ObjectValueTemplate();
		}
	}

	@Override
	protected String getPropertyName(final Method targetMethod) {
		String name = targetMethod.getName();
		if (name.startsWith("get")) {
			name = name.substring(3);
		} else if (name.startsWith("is")) {
			name = name.substring(2);
		}
		return StringUtils.uncapitalize(name);
	}

	@Override
	protected Type getPropertyType(final Method targetMethod) {
		return targetMethod.getGenericReturnType();
	}

	@Override
	protected ContextCaller createContextCallerFromContextParam(final Method targetMethod, final ContextParam contextParam) {
		if (targetMethod.isAnnotationPresent(ContextPath.class)) {
			// We have an explicite context path declaration, handle it as usual
			return super.createContextCallerFromContextParam(targetMethod, contextParam);
		} else {
			// No explicite @ContextPath, must get the context object directly
			return new OnlyReturnContextCaller(getPropertyType(targetMethod));
		}
	}

	@Override
	protected void writeJsonPropertyValue(final OutputStream output, final Object context) throws WebApplicationException, IOException {
		try {
			propertyTemplate.writeJson(output, targetMethod.invoke(null, contextCaller.callContext(context)));
		} catch (final Exception e) {
			throw new IllegalStateException("Problem while calling "+targetMethod, e);
		}
	}

}
