package com.iorga.irajblank.ws;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.StreamingOutput;

import com.iorga.iraj.annotation.ContextParam;
import com.iorga.iraj.annotation.ContextParams;
import com.iorga.iraj.json.JsonWriter;
import com.iorga.irajblank.model.entity.Profile;
import com.iorga.irajblank.model.entity.User;
import com.iorga.irajblank.model.service.UserSearchRequest;
import com.iorga.irajblank.service.ProfileService;
import com.iorga.irajblank.service.UserService;

@SuppressWarnings("unused")
@Path("/searchUser")
public class SearchUserWebService {
	@Inject
	private UserService userService;

	@Inject
	private ProfileService profileService;

	@GET
	@Path("/findAll")
	public List<User> findAll() {
		return userService.findAll();
	}

	@ContextParam(Profile.class)
	public static class ProfileResponseTemplate {
		private Integer id;
		private String label;
	}


	@ContextParams({
		@ContextParam(name = "profileList", value = List.class, parameterizedArguments = Profile.class),
		@ContextParam(name = "nbResults", value = Long.class)
	})
	public static class InitTemplate {
		private List<ProfileTemplate> profileList;
		private Long nbResults;

		@ContextParam(Profile.class)
		public static class ProfileTemplate {
			private Integer id;
			private String label;
		}
	}
	@GET
	@Path("/init")
	public StreamingOutput init() {
		final List<Profile> profileList = profileService.findAll();
		final HashMap<String, Object> context = new HashMap<String, Object>();
		context.put("profileList", profileList);
		return new JsonWriter().writeWithTemplate(InitTemplate.class, context);
	}

	@ContextParam(UserSearchResults.class)
	public static class SearchResponseTemplate {
		private List<UserTemplate> listUser;
		private Long nbResults;
		private double nbPages;

		@ContextParam(User.class)
		public static class UserTemplate {
			private Integer userId;
			private String lastName;
			private String firstName;
		}
	}
	@POST
	@Path("/search")
	public StreamingOutput search(final UserSearchRequest userFilter) {
		final UserSearchResults userSearchResults = userService.search(userFilter);
		return new JsonWriter().writeWithTemplate(SearchResponseTemplate.class, userSearchResults);
	}
}
