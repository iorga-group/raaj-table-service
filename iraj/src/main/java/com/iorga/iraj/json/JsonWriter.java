package com.iorga.iraj.json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

public class JsonWriter {
	private final Map<Class<?>, Template> templateCache = new HashMap<Class<?>, Template>();

	public StreamingOutput writeWithTemplate(final Class<?> templateClass, final Object context) {
		final Template template = getFromCacheOrCreateTemplate(templateClass);
		return new StreamingOutput() {
			@Override
			public void write(final OutputStream output) throws IOException, WebApplicationException {
				template.writeJson(output, context);
			}
		};
	}

	private Template getFromCacheOrCreateTemplate(final Class<?> templateClass) {
		Template template = templateCache.get(templateClass);
		if (template == null) {
			template = new ClassTemplate(templateClass);
			templateCache.put(templateClass, template);
		}
		return template;
	}

	public StreamingOutput writeIterableWithTemplate(final Class<?> itemTemplateClass, final Object iterableContext) {
		final Template itemTemplate = new IterableTemplate(getFromCacheOrCreateTemplate(itemTemplateClass));
		return new StreamingOutput() {
			@Override
			public void write(final OutputStream output) throws IOException, WebApplicationException {
				itemTemplate.writeJson(output, iterableContext);
			}
		};
	}
}
