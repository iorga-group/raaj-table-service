package com.iorga.iraj.framework.json;

import java.lang.reflect.Type;

public interface ContextCaller {
	public Object callContext(Object context);
	public Type getSourceType();
}
