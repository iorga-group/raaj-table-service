package com.iorga.irajblank.ws;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.iorga.irajblank.model.entity.User;


public class UserSearchResults {

	private List<User> listUser;

	private Long nbResults;

	private double nbPages;

	public List<User> getListUser() {
		return listUser;
	}

	public void setListUser(List<User> listUser) {
		this.listUser = listUser;
	}

	public double getNbPages() {
		return nbPages;
	}

	public void setNbPages(double nbPages) {
		this.nbPages = nbPages;
	}

	public Long getNbResults() {
		return nbResults;
	}

	public void setNbResults(Long nbResults) {
		this.nbResults = nbResults;
	}

}
