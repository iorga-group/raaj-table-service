package com.iorga.irajblank.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UserTemplate {

	private String login;

	private String lastName;

	private String firstName;

	private String nbPage;

	public String getLogin() {
		return login;
	}

	public void setLogin(final String lbLogi) {
		this.login = lbLogi;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lbNom) {
		this.lastName = lbNom;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String lbPren) {
		this.firstName = lbPren;
	}

	public String getNbPage() {
		return nbPage;
	}

	public void setNbPage(String nbPage) {
		this.nbPage = nbPage;
	}

}
