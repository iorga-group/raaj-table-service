package com.iorga.iraj.framework.json;

import java.lang.reflect.Type;

public class OnlyReturnContextCaller implements ContextCaller {
	private final Type contextType;

	public OnlyReturnContextCaller(final Type contextType) {
		this.contextType = contextType;
	}

	@Override
	public Object callContext(final Object context) {
		return context;
	}

	@Override
	public Type getSourceType() {
		return contextType;
	}

}
