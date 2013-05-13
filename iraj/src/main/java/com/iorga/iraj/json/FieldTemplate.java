package com.iorga.iraj.json;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;

public class FieldTemplate extends PropertyTemplate<Field> {

	public FieldTemplate(final Field targetField) {
		super(targetField);
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
