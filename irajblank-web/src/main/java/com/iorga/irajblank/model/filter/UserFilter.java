package com.iorga.irajblank.model.filter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UserFilter {

	private String nom;
	private Integer profilId;
	private String login;
	private Integer currentPage;
	private Integer nbItemPerPage;

	public UserFilter() {
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

	public Integer getProfilId() {
		return profilId;
	}

	public void setProfilId(Integer profilId) {
		this.profilId = profilId;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getNbItemPerPage() {
		return nbItemPerPage;
	}

	public void setNbItemPerPage(Integer nbItemPerPage) {
		this.nbItemPerPage = nbItemPerPage;
	}

}
