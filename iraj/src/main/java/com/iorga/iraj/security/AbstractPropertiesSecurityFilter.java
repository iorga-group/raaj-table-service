package com.iorga.iraj.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public abstract class AbstractPropertiesSecurityFilter<S extends SecurityContext> extends AbstractSecurityFilter<S> {
	protected static final String CLASSPATH_PREFIX = "classpath:";
	protected static final String FILE_PREFIX = "file:";
	protected final Properties users = new Properties();

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		try {
			final Properties config = new Properties();
			config.load(getClass().getResourceAsStream(getConfigPropertiesClasspathLocation()));
			final String usersPropertiesLocation = config.getProperty(getUsersPropertiesLocationPropertyName());
			final InputStream is;
			if (usersPropertiesLocation.startsWith(CLASSPATH_PREFIX)) {
				is = getClass().getResourceAsStream(usersPropertiesLocation.substring(CLASSPATH_PREFIX.length()));
			} else if (usersPropertiesLocation.startsWith(FILE_PREFIX)) {
				is = new FileInputStream(usersPropertiesLocation.substring(FILE_PREFIX.length()));
			} else {
				throw new IllegalStateException("Cannot recongnize 'file:' nor 'classpath:' prefix in "+usersPropertiesLocation);
			}
			users.load(is);
		} catch (final IOException e) {
			throw new ServletException("Problem while loading users", e);
		}
	}

	protected String getUsersPropertiesLocationPropertyName() {
		return "usersPropertiesLocation";
	}

	protected String getConfigPropertiesClasspathLocation() {
		return "/config.properties";
	}

	@Override
	protected S findSecurityContext(final String accessKeyId) {
		final String secretAccessKey = users.getProperty(accessKeyId);
		return secretAccessKey == null ? null : createSecurityContext(accessKeyId, secretAccessKey);
	}

	protected abstract S createSecurityContext(String accessKeyId, String secretAccessKey);
}
