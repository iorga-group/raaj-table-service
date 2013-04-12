package com.iorga.iraj.framework.json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

public class JsonWriter {
	private final Map<Class<?>, Template> templateCache = new HashMap<Class<?>, Template>();

	public StreamingOutput writeWithTemplate(final Class<?> templateClass, final Object context) {
		Template template = templateCache.get(templateClass);
		if (template == null) {
			template = new ClassTemplate(templateClass);
			templateCache.put(templateClass, template);
		}
		final Template finalTemplate = template;
		return new StreamingOutput() {
			@Override
			public void write(final OutputStream output) throws IOException, WebApplicationException {
				finalTemplate.writeJson(output, context);
			}
		};
	}
}
