package com.iorga.irajblank.bootstrap;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.iorga.iraj.util.BeanManagerUtils;
import com.iorga.irajblank.model.entity.Profile;
import com.iorga.irajblank.model.entity.User;
import com.iorga.irajblank.service.UserService;

@WebListener
public class DataInitializer implements ServletContextListener {
	private final static int MAX_ORDER = 10;

//	@Inject
//	private EntityManagerFactory entityManagerFactory;
//
//	@Inject
//	private UserService userService;


	public void createSampleDatas() throws NamingException {
		final Context ctx = new InitialContext();
		final BeanManager beanManager = (BeanManager) ctx.lookup("java:comp/env/BeanManager");
		final EntityManagerFactory entityManagerFactory = BeanManagerUtils.getOrCreateInstance(beanManager, EntityManagerFactory.class);


		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.createQuery("delete from User").executeUpdate();
		entityManager.createQuery("delete from Profile").executeUpdate();

		final Profile adminProfile = new Profile("Administrator", "admin");
		entityManager.persist(adminProfile);
		final Profile userProfile = new Profile("User", "user");
		entityManager.persist(userProfile);

//		final UserService userService = BeanManagerUtils.getOrCreateInstance(beanManager, UserService.class);

		entityManager.persist(new User("user", UserService.digestPassword("user", "user"), "User", "user", userProfile));
		entityManager.persist(new User("admin", UserService.digestPassword("admin", "admin"), "Admin", "admin", adminProfile));

		for (int i = 1; i <= MAX_ORDER; i++) {
			final Profile profile = new Profile("User"+i, "user"+i);
			entityManager.persist(profile);
			for (int j = 1; j <= MAX_ORDER; j++) {
				final int id = i*MAX_ORDER + j;
				entityManager.persist(new User("user"+id, UserService.digestPassword("user"+id, "user"+id), "User"+id, "user"+id, profile));
			}
		}

		entityManager.getTransaction().commit();
	}


	@Override
	public void contextInitialized(final ServletContextEvent sce) {
		try {
			createSampleDatas();
		} catch (final NamingException e) {
			throw new IllegalStateException("Problem while initializing", e);
		}
	}


	@Override
	public void contextDestroyed(final ServletContextEvent sce) {}
}
