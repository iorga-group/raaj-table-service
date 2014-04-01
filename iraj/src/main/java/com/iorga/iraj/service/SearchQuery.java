package com.iorga.iraj.service;

import java.util.List;

import javax.persistence.EntityManager;

import com.iorga.iraj.util.QueryDSLUtils;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.EntityPathBase;

public abstract class SearchQuery <E, B extends EntityPathBase<E>, R> {
	public abstract void configureSearchBaseQuery(B entityPathBase, R searchRequest, JPAQuery jpaQuery);
	public void configureSearchCountQuery(B entityPathBase, R searchRequest, JPAQuery jpaQuery) {}
	public void configureSearchQuery(B entityPathBase, R searchRequest, JPAQuery jpaQuery) {}

	public class Builder {
		private JPAQuery jpaQuery;
		private B entityPathBase;

		public Builder createJpaQuery(EntityManager entityManager) {
			jpaQuery = SearchQuery.this.createJpaQuery(entityManager);
			return this;
		}

		public Builder configureSearchBaseQuery(B entityPathBase, R searchRequest) {
			this.entityPathBase = entityPathBase;
			SearchQuery.this.configureSearchBaseQuery(entityPathBase, searchRequest, jpaQuery);
			return this;
		}

		public Builder configureSearchCountQuery(B entityPathBase, R searchRequest) {
			this.entityPathBase = entityPathBase;
			SearchQuery.this.configureSearchCountQuery(entityPathBase, searchRequest, jpaQuery);
			return this;
		}

		public Builder configureSearchQuery(B entityPathBase, R searchRequest) {
			this.entityPathBase = entityPathBase;
			SearchQuery.this.configureSearchQuery(entityPathBase, searchRequest, jpaQuery);
			return this;
		}

		public Builder addOrderBysOffsetAndLimit(SearchScope searchScope) {
			QueryDSLUtils.addOrderBysOffsetAndLimit(jpaQuery, searchScope, entityPathBase);
			return this;
		}

		public JPAQuery getJpaQuery() {
			return jpaQuery;
		}
	}

	public List<E> search(B entityPathBase, R searchRequest, SearchScope searchScope, EntityManager entityManager) {
		return createSearchBuilder(entityManager, entityPathBase, searchRequest)
			.configureSearchQuery(entityPathBase, searchRequest)
			.addOrderBysOffsetAndLimit(searchScope)
			.getJpaQuery()
			.list(entityPathBase);
	}

	public long searchCount(B entityPathBase, R searchRequest, EntityManager entityManager) {
		return createSearchBuilder(entityManager, entityPathBase, searchRequest)
			.configureSearchCountQuery(entityPathBase, searchRequest)
			.getJpaQuery()
			.count();
	}

	public Builder createSearchBuilder(EntityManager entityManager, B entityPathBase, R searchRequest) {
		return new Builder()
			.createJpaQuery(entityManager)
			.configureSearchBaseQuery(entityPathBase, searchRequest);
	}

	protected JPAQuery createJpaQuery(EntityManager entityManager) {
		return new JPAQuery(entityManager, JPQLTemplates.DEFAULT);
	}
}
