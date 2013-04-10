package com.iorga.irajblank.data;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class EntityManagerFactoryProducer {

	@Produces @ApplicationScoped
	public EntityManagerFactory createEntityManagerFactory() {
		return Persistence.createEntityManagerFactory("com.iorga.irajblank");
	}

	public void disposeEntityManagerFactory(@Disposes final EntityManagerFactory entityManagerFactory) {
		if (entityManagerFactory.isOpen()) {
			entityManagerFactory.close();
		}
	}

	@Produces @RequestScoped
	public EntityManager createEntityManager(final EntityManagerFactory entityManagerFactory) {
		return entityManagerFactory.createEntityManager();
	}

	public void disposeEntityManager(@Disposes final EntityManager entityManager) {
		if (entityManager.isOpen()) {
			entityManager.close();
		}
	}
}
