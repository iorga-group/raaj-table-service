package com.iorga.irajblank.model.service;

import com.iorga.iraj.service.SearchScope;


public class UserSearchRequest extends PaginatedResquest {

	private String nom;
	private Integer profileId;
	private String login;
	private SearchScope searchScope;

	public UserSearchRequest() {
	}

	public String getNom() {
		return nom;
	}
	public void setNom(final String nom) {
		this.nom = nom;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(final Integer profileId) {
		this.profileId = profileId;
	}

	public SearchScope getSearchScope() {
		return searchScope;
	}

	public void setSearchScope(SearchScope searchScope) {
		this.searchScope = searchScope;
	}

}
