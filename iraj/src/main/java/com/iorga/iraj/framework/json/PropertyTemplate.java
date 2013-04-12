package com.iorga.iraj.framework.json;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;

import com.iorga.iraj.framework.annotation.ContextParam;
import com.iorga.iraj.framework.annotation.ContextParams;
import com.iorga.iraj.framework.annotation.JsonProperty;

public abstract class PropertyTemplate<T extends AnnotatedElement & Member> implements Template {
	protected final ContextCaller contextCaller;
	protected final byte[] jsonPropertyName;

	public PropertyTemplate(final T targetAnnotatedMember) {
		final JsonProperty jsonProperty = targetAnnotatedMember.getAnnotation(JsonProperty.class);
		if (jsonProperty != null) {
			jsonPropertyName = jsonProperty.value().getBytes();
		} else {
			jsonPropertyName = getPropertyName(targetAnnotatedMember).getBytes();
		}
		final Class<?> declaringClass = targetAnnotatedMember.getDeclaringClass();
		final ContextParams contextParams = declaringClass.getAnnotation(ContextParams.class);
		if (contextParams != null) {
			contextCaller = new MapContextCaller<T>(targetAnnotatedMember, contextParams, this);
		} else {
			final ContextParam contextParam = declaringClass.getAnnotation(ContextParam.class);
			if (contextParam == null) {
				throw new IllegalArgumentException("Can't find @ContextParam or @ContextParams on type "+declaringClass.getName()+" for member "+targetAnnotatedMember.getName());
			} else {
				contextCaller = createContextCallerFromContextParam(targetAnnotatedMember, contextParam);
			}
		}
	}

	protected ContextCaller createContextCallerFromContextParam(final T targetAnnotatedMember, final ContextParam contextParam) {
		return new ObjectContextCaller(targetAnnotatedMember, contextParam, this);
	}

	protected abstract String getPropertyName(T targetAnnotatedMember);

	protected abstract Type getPropertyType(T targetAnnotatedMember);

	protected abstract void writeJsonPropertyValue(OutputStream output, Object context) throws WebApplicationException, IOException;

	@Override
	public void writeJson(final OutputStream output, final Object context) throws IOException, WebApplicationException {
		output.write('"');
		output.write(jsonPropertyName);
		output.write('"');
		output.write(':');
		writeJsonPropertyValue(output, context);
//		getPropertyTemplate().writeJson(output, contextCaller.callContext(context));
	}
}
