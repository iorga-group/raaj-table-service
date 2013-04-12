package com.iorga.iraj.framework.json;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;

public class FieldTemplate extends PropertyTemplate<Field> {
	protected final Template propertyTemplate;

	public FieldTemplate(final Field targetField) {
		super(targetField);

		final Type propertyType = getPropertyType(targetField);
		if (contextCaller.getSourceType().equals(propertyType)) {
			//TODO gérer les types listes
			//TODO assert type simple
//			final Class<?> propertyClass = (Class<?>)propertyType;
			propertyTemplate = new ObjectValueTemplate();
//			if (propertyClass.isPrimitive() || Primitives.isWrapperType(propertyClass)) {
//				// We have a primitive
//			}
			// Same type on both source and target, let's assert that's it's a simple type (else, must have a template for that, so => exception)

//			propertyTemplate = new Copy
//			propertyTemplate = null; //FIXME
		} else {
			// Different types, so we have another template to introspect
			propertyTemplate = new ClassTemplate((Class<?>)propertyType);
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
