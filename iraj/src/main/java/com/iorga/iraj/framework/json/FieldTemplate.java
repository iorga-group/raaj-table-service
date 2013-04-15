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

//
//		if (sourceType.equals(targetType)) {//TODO gérer ça directement avec le type paramétré de google puis par la suite vérifier que c'est un Iterable
//			// Handle iterables : if the parameter type is equal, we can create directly an ObjectValueTemplate, else we must pass by an IterableTemplate
//			if (targetTypeToken.getRawType().isAssignableFrom(Iterable.class)) {
//				// We have an iterable on both side, let's check if we have the same item type
//				if (targetTypeToken.isAssignableFrom(sourceTypeToken)) {
//					// We have the same element types, we can let jackson handle it
//					propertyTemplate = new ObjectValueTemplate();
//				} else {
//					// TODO : gérer les Iterables d'Iterables (donc faire un while ici)
//					final TypeToken<?> itemTargetTypeToken = targetTypeToken.resolveType(Iterable.class.getTypeParameters()[0]);
//					propertyTemplate = new IterableTemplate(new ClassTemplate(itemTargetTypeToken.getRawType()));
//				}
//			} else {
//				propertyTemplate = new ObjectValueTemplate();
//			}
//		} else {
//			// Different types, so we have another template to introspect
//			propertyTemplate = new ClassTemplate((Class<?>)targetType);
//		}
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
