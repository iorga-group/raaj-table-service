package com.iorga.iraj.json;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.WebApplicationException;

public class ClassTemplate implements Template {
	private final List<PropertyTemplate<?, ?>> templatesToCall = new LinkedList<PropertyTemplate<?, ?>>();

	public ClassTemplate(final Class<?> targetClass) {
		processClass(targetClass);
	}

	private void processClass(final Class<?> targetClass) {
		final Class<?> superclass = targetClass.getSuperclass();
		if (superclass != null && TemplateUtils.isTemplate(superclass)) {
			processClass(superclass);
		}
		for(final Field targetField : targetClass.getDeclaredFields()) {
			if (PropertyTemplate.isPropertyTemplate(targetField)) {
				if ((targetField.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
					// this is a static field
					templatesToCall.add(new StaticFieldTemplate(targetField));
				} else {
					templatesToCall.add(new FieldTemplate(targetField));
				}
			}
		}
		for(final Method targetMethod : targetClass.getDeclaredMethods()) {
			if (MethodTemplate.isMethodTemplate(targetMethod)) {
				templatesToCall.add(new MethodTemplate(targetMethod));
			}
		}
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
