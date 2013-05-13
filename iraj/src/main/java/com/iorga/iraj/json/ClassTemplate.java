package com.iorga.iraj.json;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import com.iorga.iraj.annotation.ContextPath;
import com.iorga.iraj.annotation.IgnoreProperty;
import com.iorga.iraj.annotation.JsonProperty;

public class ClassTemplate implements Template {
	private final List<PropertyTemplate<?>> templatesToCall = new LinkedList<PropertyTemplate<?>>();

	public ClassTemplate(final Class<?> targetClass) {
		for(final Field targetField : targetClass.getDeclaredFields()) {
			if (haveToInclude(targetField)) {
				if ((targetField.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
					// this is a static field
					templatesToCall.add(new StaticFieldTemplate(targetField));
				} else {
					templatesToCall.add(new FieldTemplate(targetField));
				}
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
