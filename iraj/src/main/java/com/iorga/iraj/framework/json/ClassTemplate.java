package com.iorga.iraj.framework.json;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import com.iorga.iraj.framework.annotation.ContextPath;
import com.iorga.iraj.framework.annotation.IgnoreProperty;
import com.iorga.iraj.framework.annotation.JsonProperty;

public class ClassTemplate implements Template {
	private final List<PropertyTemplate<?>> templatesToCall = new LinkedList<PropertyTemplate<?>>();

	public ClassTemplate(final Class<?> targetClass) {
		for(final Field targetField : targetClass.getDeclaredFields()) {
			if (haveToInclude(targetField)) {
				templatesToCall.add(new FieldTemplate(targetField));
			}
		}
		for(final Method targetMethod : targetClass.getDeclaredMethods()) {
			if (haveToInclude(targetMethod)) {
				templatesToCall.add(new MethodTemplate(targetMethod));
			}
		}
	}

	private boolean haveToInclude(final AnnotatedElement annotatedElement) {
		return annotatedElement.isAnnotationPresent(ContextPath.class) || annotatedElement.isAnnotationPresent(JsonProperty.class) || !annotatedElement.isAnnotationPresent(IgnoreProperty.class);
	}

	@Override
	public void writeJson(final OutputStream output, final Object context) throws IOException, WebApplicationException {
		// TODO Auto-generated method stub
		output.write('{');
		boolean first = true;
		for (final Template template : templatesToCall) {
			if (first) {
				first = false;
			} else {
				output.write(',');
			}
			template.writeJson(output, context);
		}
		output.write('}');
	}

}
