package com.iorga.irajblank.ws;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.iorga.irajblank.model.User;
import com.iorga.irajblank.service.UserService;

@Path("/user")
public class UserWebService {
	@Inject
	private UserService userService;

	public static class FindForIdResult {

	}

	@GET
	@Path("/find/{id}")
	public User find(@PathParam("id") final Integer id) {
		return userService.find(id);
	}
}
