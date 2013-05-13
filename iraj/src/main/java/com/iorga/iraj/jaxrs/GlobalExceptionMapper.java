package com.iorga.iraj.jaxrs;

import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.iorga.iraj.annotation.ContextParam;
import com.iorga.iraj.annotation.TargetType;
import com.iorga.iraj.util.JaxRsUtils;

@SuppressWarnings("unused")
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
	private static Logger log = LoggerFactory.getLogger(GlobalExceptionMapper.class);

	@ContextParam(Throwable.class)
	public static class ThrowableTemplate {
		@TargetType(value = Iterable.class, parameterizedArguments = MessageTemplate.class)
		public static Iterable<Throwable> getIrajMessages(final Throwable throwable) {
			return Lists.newArrayList(throwable);
		}

		@ContextParam(Throwable.class)
		public static class MessageTemplate {
			private static final String type = "error";

			public static String getMessage(final Throwable throwable) {
				final String uuid = UUID.randomUUID().toString();
				log.warn("Throwable #"+uuid+" catched by GlobalExceptionMapper", throwable);
				return "Problème non attendu #" + uuid; //TODO système de traduction
			}
		}
	}
	@Override
	public Response toResponse(final Throwable throwable) {
		return JaxRsUtils.throwableToIrajResponse(ThrowableTemplate.class, throwable);
	}

}
