package com.iorga.irajblank.model.service;

import javax.validation.constraints.Size;


public class UserSaveRequest {

	private Integer userId;
	@Size(max = 20)
	private String login;
	private String password;
	private String lastName;
	private String firstName;
	private Integer profileId;
	private Boolean active;

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(final Integer userId) {
		this.userId = userId;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}
	public Integer getProfileId() {
		return profileId;
	}
	public void setProfileId(final Integer profileId) {
		this.profileId = profileId;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(final Boolean active) {
		this.active = active;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(final String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(final String password) {
		this.password = password;
	}

}
