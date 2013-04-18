package com.iorga.irajblank.ws;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.iorga.irajblank.model.entity.User;
import com.iorga.irajblank.model.filter.UserFilter;
import com.iorga.irajblank.service.UserService;

@Path("/user")
public class SaveUserWebService {
	@Inject
	private UserService userService;

	@GET
	@Path("/find/{id}")
	@Produces("application/json")
	public User find(@PathParam("id") final Integer id) {
		return userService.find(id);
	}

	@GET
	@Path("/findAll")
	@Produces("application/json")
	public List<User> findAll() {
		return userService.findAll();
	}

	@POST
	@Path("/search")
	@Consumes("application/json")
	@Produces("application/json")
	public UserSearchTemplate search(UserFilter userFilter) {
		UserSearchTemplate userSearchTemplate = userService.getUserSearchTemplate(userFilter);
		return userSearchTemplate;
	}
}
