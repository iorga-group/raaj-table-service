package com.iorga.irajblank.model.entity;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@NamedQueries({
	@NamedQuery(
		name = User.QUERY_ACTIVE_BY_LOGIN_AND_PASSWORD,
		query = "from User where login = :login and password = :password and active is true"),
	@NamedQuery(
		name = User.EXISTS_WITH_LOGIN,
		query = "select 1 from User where login = :login"),
	@NamedQuery(
		name = User.UPDATE_SET_ACTIVE_FOR_USERS,
		query = "update User user set user.active = :active where user in (:users)")
})
@Entity
@Table(name = "USER")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable, Principal {
	private static final long serialVersionUID = 1L;

	public static final String QUERY_ACTIVE_BY_LOGIN_AND_PASSWORD = "User.QUERY_ACTIVE_BY_LOGIN_AND_PASSWORD";
	public static final String EXISTS_WITH_LOGIN = "User.EXISTS_WITH_LOGIN";
	public static final String UPDATE_SET_ACTIVE_FOR_USERS = "User.UPDATE_SET_ACTIVE_FOR_USERS";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer userId;

	@Version
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VERSION", length = 19)
	private Date version;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PROF", nullable = false)
	@NotNull
	private Profile profile;

	@Column(name = "LB_LOGI", nullable = false, length = 20)
	@NotNull
	@Size(max = 20)
	private String login;

	@Column(name = "LB_PASS", nullable = false, length = 100)
	@NotNull
	@Size(max = 100)
	private String password;

	@Column(name = "LB_LAST_NAME", nullable = false, length = 50)
	@NotNull
	@Size(max = 50)
	private String lastName;

	@Column(name = "LB_FIRS_NAME", nullable = false, length = 50)
	@NotNull
	@Size(max = 50)
	private String firstName;

	@Column(name = "BL_ACTI", nullable = false)
	@NotNull
	private Boolean active;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_CREA", nullable = true, length = 10)
	private Date creationDate;

	@Column(name = "CD_USERCREA", nullable = true, length = 10)
	private String creationLogin;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_MODI", nullable = true, length = 10)
	private Date modificationDate;

	@Column(name = "CD_USERMODI", nullable = true, length = 10)
	private String modificationLogin;


	public User(final String login, final String password, final String lastName, final String firstName, final Profile profile) {
		this.login = login;
		this.password = password;
		this.lastName = lastName;
		this.firstName = firstName;
		this.profile = profile;
		this.active = Boolean.TRUE;
	}

	public User() {}


	@Override
	public String getName() {
		return getLogin();
	}


	public Integer getUserId() {
		return userId;
	}

	public void setUserId(final Integer id) {
		this.userId = id;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(final Date version) {
		this.version = version;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(final Profile profile) {
		this.profile = profile;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(final String lbLogi) {
		this.login = lbLogi;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String lbPass) {
		this.password = lbPass;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(final Boolean blActi) {
		this.active = blActi;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(final Date dtCrea) {
		this.creationDate = dtCrea;
	}

	public String getCreationLogin() {
		return creationLogin;
	}

	public void setCreationLogin(final String creationLogin) {
		this.creationLogin = creationLogin;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(final Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getModificationLogin() {
		return modificationLogin;
	}

	public void setModificationLogin(final String modificationLogin) {
		this.modificationLogin = modificationLogin;
	}
}
