package com.iorga.irajblank.model.service;

import java.util.List;

import com.iorga.irajblank.model.entity.User;


public class UserSearchResponse {

	private List<User> listUser;

	private Long nbResults;

	private double nbPages;

	public List<User> getListUser() {
		return listUser;
	}

	public void setListUser(final List<User> listUser) {
		this.listUser = listUser;
	}

	public double getNbPages() {
		return nbPages;
	}

	public void setNbPages(final double nbPages) {
		this.nbPages = nbPages;
	}

	public Long getNbResults() {
		return nbResults;
	}

	public void setNbResults(final Long nbResults) {
		this.nbResults = nbResults;
	}

}
