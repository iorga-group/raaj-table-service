package com.iorga.iraj.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.iorga.iraj.annotation.ContextParam;
import com.iorga.iraj.util.JaxRsUtils;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@ContextParam(ConstraintViolationException.class)
	public static class ConstraintViolationExceptionTemplate {
		String message;
		String localizedMessage;
		Set<ConstraintViolationTemplate> constraintViolations;

		@ContextParam(ConstraintViolation.class)
		public static class ConstraintViolationTemplate {
			String message;
			String messageTemplate;
			Iterable<NodeTemplate> propertyPath;

			@ContextParam(Node.class)
			public static class NodeTemplate {
				String name;
				boolean inIterable;
				Integer index;
				Object key;
			}
		}
	}
	@Override
	public Response toResponse(final ConstraintViolationException exception) {
		return JaxRsUtils.throwableToIrajResponse(ConstraintViolationExceptionTemplate.class, exception);
	}

}
