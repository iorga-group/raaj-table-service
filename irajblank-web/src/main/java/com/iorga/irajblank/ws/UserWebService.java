package com.iorga.irajblank.ws;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.StreamingOutput;

import com.iorga.iraj.annotation.ContextParam;
import com.iorga.iraj.annotation.ContextParams;
import com.iorga.iraj.annotation.ContextPath;
import com.iorga.iraj.annotation.JsonProperty;
import com.iorga.iraj.json.JsonWriter;
import com.iorga.irajblank.model.entity.Profile;
import com.iorga.irajblank.model.entity.User;
import com.iorga.irajblank.model.service.UserSaveRequest;
import com.iorga.irajblank.service.ProfileService;
import com.iorga.irajblank.service.UserService;
import com.iorga.irajblank.ws.SearchUserWebService.InitTemplate.ProfileTemplate;

@SuppressWarnings("unused")
@Path("/user")
public class UserWebService {
	@Inject
	private UserService userService;

	@Inject
	private ProfileService profileService;

	@ContextParam(User.class)
	public static class UserResponseTemplate {
		private Integer userId;

		private String login;

		private String password;

		@JsonProperty("nom")
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
		User user = userService.find(id);
		return new JsonWriter().writeWithTemplate(UserResponseTemplate.class, user);
	}

	@ContextParams({
		@ContextParam(name = "profileList", value = List.class, parameterizedArguments = Profile.class)
	})
	public static class InitTemplate{
		List<ProfileTemplate> profileList;

		@ContextParam(Profile.class)
		public static class ProfilTemplate{
			private Integer id;
			private String label;
		}
	}
	@GET
	@Path("/init")
	public StreamingOutput init() {
		List<Profile> profileList = profileService.findAll();
		HashMap<String, Object> context = new HashMap<String, Object>();
		context.put("profileList", profileList);
		return new JsonWriter().writeWithTemplate(InitTemplate.class, context);
	}

	@POST
	@Path("/save")
	public Integer save(UserSaveRequest usar) {
		return userService.save(usar);
	}
}
