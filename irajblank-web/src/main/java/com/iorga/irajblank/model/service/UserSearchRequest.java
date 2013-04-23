package com.iorga.irajblank.model.service;


public class UserSearchRequest extends PaginatedResquest{

	private String nom;
	private Integer profileId;
	private String login;

	public UserSearchRequest() {
	}

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

}
