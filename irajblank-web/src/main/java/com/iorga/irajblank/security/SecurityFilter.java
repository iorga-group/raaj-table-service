package com.iorga.irajblank.security;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iorga.iraj.security.AbstractSecurityFilter;
import com.iorga.iraj.security.SimpleSecurityContext;
import com.iorga.irajblank.service.UserService;


@WebFilter(urlPatterns = "/api/*")
public class SecurityFilter extends AbstractSecurityFilter<SimpleSecurityContext> {
	@Inject
	private UserService userService;

	@Override
	protected SimpleSecurityContext findSecurityContext(final String login) {
		// Here we will use the login as the accessKeyId and the encrypted password as the secretAccessKey
		return new SimpleSecurityContext(userService.findPasswordForLogin(login));
	}

	@Override
	protected boolean handleParsedDate(final Date parsedDate, final SimpleSecurityContext securityContext, final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) throws IOException {
		// If the target webservice is the "/api/security/getTime" webservice, do not handle the date check
		if (httpRequest.getPathInfo().equals(SecurityWebService.SECURITY_WEB_SERVICE_PATH_PREFIX+SecurityWebService.GET_TIME_METHOD_PATH)) {
			return true;
		} else {
			return super.handleParsedDate(parsedDate, securityContext, httpRequest, httpResponse);
		}
	}
}
