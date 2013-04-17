package com.iorga.iraj.json;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;

import org.codehaus.jackson.map.ObjectMapper;


public class ObjectValueTemplate implements Template {
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Override
	public void writeJson(final OutputStream output, final Object context) throws IOException, WebApplicationException {
		OBJECT_MAPPER.writeValue(output, context);
	}

}
