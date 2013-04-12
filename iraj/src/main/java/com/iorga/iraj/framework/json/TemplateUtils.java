package com.iorga.iraj.framework.json;

import com.iorga.iraj.framework.annotation.ContextParam;
import com.iorga.iraj.framework.annotation.ContextParams;

public class TemplateUtils {
	public static boolean isTemplate(final Class<?> klass) {
		return klass.isAnnotationPresent(ContextParams.class) || klass.isAnnotationPresent(ContextParam.class);
	}
}
