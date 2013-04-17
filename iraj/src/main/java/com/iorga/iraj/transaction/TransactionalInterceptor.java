package com.iorga.iraj.transaction;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.iorga.iraj.annotation.Transactional;

@Interceptor
@Transactional
public class TransactionalInterceptor {

	@AroundInvoke
	public void interceptTrancational(final InvocationContext invocationContext, final EntityManager entityManager) throws Exception {
		final EntityTransaction transaction = entityManager.getTransaction();
		boolean transactionBegun = false;
		if (!transaction.isActive() && !ManualTransactionalInterceptor.isCurrentThreadInManualTransaction()) {
			transaction.begin();
			transactionBegun = true;
		}
		try {
			invocationContext.proceed();
			if (transactionBegun) {
				transaction.commit();
			}
		} catch (final Throwable throwable) {
			if (transactionBegun) {
				transaction.rollback();
			}
		}
	}

}
