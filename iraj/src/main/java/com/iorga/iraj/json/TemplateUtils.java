package com.iorga.iraj.json;

import com.iorga.iraj.annotation.ContextParam;
import com.iorga.iraj.annotation.ContextParams;

public class TemplateUtils {
	public static boolean isTemplate(final Class<?> klass) {
		return klass.isAnnotationPresent(ContextParams.class) || klass.isAnnotationPresent(ContextParam.class);
	}
}
