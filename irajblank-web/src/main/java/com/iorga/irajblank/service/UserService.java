package com.iorga.irajblank.service;

import java.util.Collection;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.apache.commons.codec.digest.DigestUtils;

import com.iorga.iraj.service.JPAEntityService;
import com.iorga.irajblank.model.User;

public class UserService extends JPAEntityService<User, Integer> {

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
		return password != null ? DigestUtils.shaHex(password) : null;	// En cas de null, DigestUtils envoie un NPE
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
}
