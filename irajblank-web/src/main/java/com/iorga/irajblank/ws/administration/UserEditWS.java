package com.iorga.irajblank.ws.administration;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.StreamingOutput;

import org.jboss.resteasy.spi.validation.ValidateRequest;

import com.iorga.iraj.annotation.ContextParam;
import com.iorga.iraj.annotation.ContextPath;
import com.iorga.iraj.exception.FunctionalException;
import com.iorga.iraj.json.JsonWriter;
import com.iorga.irajblank.model.entity.Profile;
import com.iorga.irajblank.model.entity.User;
import com.iorga.irajblank.model.service.UserSaveRequest;
import com.iorga.irajblank.service.ProfileService;
import com.iorga.irajblank.service.UserService;

@SuppressWarnings("unused")
@Path("/administration/userEdit")
@ApplicationScoped
@ValidateRequest
public class UserEditWS {
	@Inject
	private UserService userService;

	@Inject
	private ProfileService profileService;

	@Inject
	private JsonWriter jsonWriter;

	@ContextParam(User.class)
	public static class FindTemplate {
		private Integer userId;

		private String login;

		private String password;

		private String lastName;

		private String firstName;

		@ContextPath("profile.id")
		private Integer profileId;

		private Boolean active;
	}
	@GET
	@Path("/find/{id}")
	@Produces("application/json")
	public StreamingOutput find(@PathParam("id") final Integer id) {
		final User user = userService.find(id);
		return jsonWriter.writeWithTemplate(FindTemplate.class, user);
	}

	@ContextParam(Profile.class)
	public static class InitProfileTemplate {
		private Integer id;
		private String label;
	}
	@GET
	@Path("/init")
	public StreamingOutput init() {
		final List<Profile> profileList = profileService.findAll();
		return jsonWriter.writeIterableWithTemplate(InitProfileTemplate.class, profileList);
	}

	@POST
	@Path("/save")
	public Integer save(@Valid final UserSaveRequest userSaveRequest) throws FunctionalException {
		return userService.save(userSaveRequest);
	}
}
