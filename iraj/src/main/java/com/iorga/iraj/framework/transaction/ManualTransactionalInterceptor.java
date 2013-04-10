package com.iorga.iraj.framework.transaction;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

import com.iorga.iraj.framework.annotation.ManualTransactional;

@Interceptor
@ManualTransactional
public class ManualTransactionalInterceptor {
	private static final ThreadLocal<Integer> nbCallTL = new ThreadLocal<Integer>();

	@AroundInvoke
	public void interceptManualTrancational(final InvocationContext invocationContext, final EntityManager entityManager) throws Exception {
		final Integer nbCall = nbCallTL.get();
		if (nbCall == null) {
			if (entityManager.getTransaction().isActive()) {
				throw new IllegalStateException("The transaction has already been begun, couldn't enter in \"manual\" mode.");
			}
			nbCallTL.set(1);
		} else {
			nbCallTL.set(nbCall + 1);
		}
		try {
			invocationContext.proceed();
		} finally {
			final Integer nbCall2 = nbCallTL.get();
			if (nbCall == null) {
				throw new IllegalStateException("The current nbCall couldn't be null");
			} else {
				if (nbCall2 == 1) {
					nbCallTL.remove();
				} else {
					nbCallTL.set(nbCall2 - 1);
				}
			}
		}
	}

	public static boolean isCurrentThreadInManualTransaction() {
		return nbCallTL.get() != null;
	}
}
