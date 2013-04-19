package com.iorga.irajblank.service;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.apache.commons.codec.digest.DigestUtils;

import com.iorga.iraj.service.JPAEntityService;
import com.iorga.irajblank.model.entity.QUser;
import com.iorga.irajblank.model.entity.User;
import com.iorga.irajblank.model.service.UserSaveRequest;
import com.iorga.irajblank.model.service.UserSearchRequest;
import com.iorga.irajblank.ws.UserSearchResults;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.impl.JPAQuery;

public class UserService extends JPAEntityService<User, Integer> {

	@Inject
	private ProfileService profileService;

	public User findWithLoginAndPassword(final String login, final String password) {
		final String digestedPassword = digestPassword(password);

		try {
			return getEntityManager().createNamedQuery(User.QUERY_ACTIVE_BY_LOGIN_AND_PASSWORD, User.class)
				.setParameter("login", login)
				.setParameter("password", digestedPassword)
				.getSingleResult();
		} catch (final NoResultException e) {
			return null;
		} catch (final NonUniqueResultException e) {
			return null;
		}
	}

	public String digestPassword(final String password) {
		return password != null ? DigestUtils.sha1Hex(password) : null;	// En cas de null, DigestUtils envoie un NPE
	}

	public User changeBlActi(final User user, final boolean activate) {
		user.setActive(activate);
		return update(user);
	}

	@Override
	public void create(final User entity) {
		if (entity.getActive() == null) {
			entity.setActive(true);
		}
		super.create(entity);
	}

	public void create(final User user, final String clearPassword) {
		user.setPassword(digestPassword(clearPassword));
		create(user);
	}

	public void update(final User user, final String clearPassword) {
		user.setPassword(digestPassword(clearPassword));
		update(user);
	}

	public Boolean checkLoginExists(final String login) {
		try {
			getEntityManager().createNamedQuery(User.EXISTS_WITH_LOGIN)
				.setParameter("login", login)
				.getSingleResult();
		} catch (final NoResultException e) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public void changeBlActi(final Collection<User> users, final boolean active) {
		if (users != null && users.size() > 0) {
			final int updatedUsers = getEntityManager().createNamedQuery(User.UPDATE_SET_ACTIVE_FOR_USERS)
				.setParameter("active", active)
				.setParameter("users", users)
				.executeUpdate();
			assert updatedUsers == users.size();
		}
	}

	public UserSearchResults find(final UserSearchRequest userFilter){
		final UserSearchResults userSearchResults = new UserSearchResults();
		final Long nbUser = this.countUser(userFilter);
		final List<User> listUser = this.searchUser(userFilter);

		userSearchResults.setNbPages(Math.ceil((float)nbUser/userFilter.getPageSize()));
		userSearchResults.setNbResults(nbUser);
		userSearchResults.setListUser(listUser);

		return userSearchResults;
	}

	public long countUser(final UserSearchRequest userFilter) {
		final QUser qUser = QUser.user;

		final JPAQuery query = this.composeQuery(qUser, userFilter);

		return query.count();
	}

	public List<User> searchUser(final UserSearchRequest userFilter) {

		final QUser qUser = QUser.user;

		//Tous les many-to-one et one-to-many sont en lazy load (chargé uniquement si on accède à la propriété)

		final JPAQuery query = this.composeQuery(qUser, userFilter);

		query.offset((userFilter.getCurrentPage()-1)*userFilter.getPageSize());
		query.limit(userFilter.getPageSize());
		return query.list(qUser);
	}

	private JPAQuery composeQuery(final QUser qUser, final UserSearchRequest userFilter){

		final JPAQuery query = new JPAQuery(getEntityManager(), HQLTemplates.DEFAULT);

		query.from(qUser);

		if (userFilter.getNom() != null){
			query.where(qUser.lastName.containsIgnoreCase(userFilter.getNom()));
		}
		if (userFilter.getProfileId() != null){
			query.where(qUser.profile.id.eq(userFilter.getProfileId()));
		}
		if (userFilter.getLogin() != null){
			query.where(qUser.lastName.containsIgnoreCase(userFilter.getLogin()));
		}

		return query;
	}

	public Integer save(UserSaveRequest usar) {
		User user = new User();
		boolean newUser = true;
		if (usar.getUserId() != 0) {
			user = this.find(usar.getUserId());
			newUser = false;
		}

		user.setLogin(usar.getLogin());
		user.setPassword(usar.getPassword());
		user.setLastName(usar.getNom());
		user.setFirstName(usar.getFirstName());
		user.setActive(usar.getActive());
		user.setProfile(profileService.getReference(usar.getProfileId()));

		if (newUser) {
			this.create(user);
		}

		return user.getUserId();
	}

}
