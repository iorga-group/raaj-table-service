package com.iorga.iraj.framework.json;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;

import com.google.common.reflect.TypeToken;

public class FieldTemplate extends PropertyTemplate<Field> {
	protected final Template propertyTemplate;

	public FieldTemplate(final Field targetField) {
		super(targetField);

		final Type targetType = getPropertyType(targetField);
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

	@Override
	protected String getPropertyName(final Field targetField) {
		return targetField.getName();
	}

	@Override
	protected Type getPropertyType(final Field targetField) {
		return targetField.getGenericType();
	}

	@Override
	protected void writeJsonPropertyValue(final OutputStream output, final Object context) throws WebApplicationException, IOException {
		propertyTemplate.writeJson(output, contextCaller.callContext(context));
	}

}
