package com.iorga.iraj.framework.json;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;

public interface Template {
	public void writeJson(OutputStream output, Object context) throws IOException, WebApplicationException;
}
