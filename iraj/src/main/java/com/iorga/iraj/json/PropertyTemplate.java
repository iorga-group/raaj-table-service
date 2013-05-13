package com.iorga.iraj.json;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;

import com.google.common.reflect.TypeToken;
import com.iorga.iraj.annotation.ContextParam;
import com.iorga.iraj.annotation.ContextParams;
import com.iorga.iraj.annotation.JsonProperty;
import com.iorga.iraj.annotation.TargetType;

public abstract class PropertyTemplate<T extends AnnotatedElement & Member> implements Template {
	protected final ContextCaller contextCaller;
	protected final byte[] jsonPropertyName;
	protected final Template propertyTemplate;

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

		// Now compute the target type
		final Type targetType;
		final TargetType targetTypeAnnotation = targetAnnotatedMember.getAnnotation(TargetType.class);
		if (targetTypeAnnotation != null) {
			targetType = new ParameterizedType() {
				@Override
				public Type getRawType() {
					return targetTypeAnnotation.value();
				}
				@Override
				public Type getOwnerType() {
					return targetTypeAnnotation.value().getDeclaringClass();
				}
				@Override
				public Type[] getActualTypeArguments() {
					return targetTypeAnnotation.parameterizedArguments();
				}
			};
		} else {
			targetType = getPropertyType(targetAnnotatedMember);
		}
		final Type sourceType = contextCaller.getSourceType();
		final TypeToken<?> targetTypeToken = TypeToken.of(targetType);
		final TypeToken<?> sourceTypeToken = TypeToken.of(sourceType);

		if (targetTypeToken.isAssignableFrom(sourceTypeToken)) {
			// Same & compatible types, let's use ObjectValueTemplate (which uses Jackson to do the work)
			propertyTemplate = new ObjectValueTemplate();
		} else {
			// First, see if we have an Iterable (List, Set...) in order to compare the type argument value to see if we must create a template with it
			if (Iterable.class.isAssignableFrom(targetTypeToken.getRawType())) {
				// We've got Iterables on both side, so the propertyTemplate is a class one, which will convert from the sourceItemType to the targetItemType
				final Class<?> sourceItemClass = targetTypeToken.resolveType(Iterable.class.getTypeParameters()[0]).getRawType();
				propertyTemplate = new IterableTemplate(new ClassTemplate(sourceItemClass));
			} else {
				// We've got different types on both sides and it's not an iterable, we can use the ClassTemplate
				propertyTemplate = new ClassTemplate(targetTypeToken.getRawType());
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
