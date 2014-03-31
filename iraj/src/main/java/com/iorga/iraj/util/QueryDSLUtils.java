/*
 * Copyright (C) 2013 Iorga Group
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/].
 */
package com.iorga.iraj.util;

import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.iorga.iraj.service.SearchScope;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.ComparableExpressionBase;

public class QueryDSLUtils {
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<?>> OrderSpecifier<T> parseOrderSpecifier(final String orderByPath, final String orderByDirection, final Expression<?> baseExpression) {
		final String[] orderByPaths = orderByPath.split("\\.");
		Expression<?> listExpression = baseExpression;

		for (final String orderByPathElement : orderByPaths) {
			// get that public field part
			try {
				listExpression = (Expression<?>) listExpression.getClass().getField(orderByPathElement).get(listExpression);
			} catch (final Exception e) {
				throw new IllegalStateException("Cannot get " + listExpression + "." + orderByPathElement, e);
			}
		}

		if (StringUtils.equalsIgnoreCase(orderByDirection, "DESC")) {
			return ((ComparableExpressionBase<T>)listExpression).desc();
		} else {
			return ((ComparableExpressionBase<T>)listExpression).asc();
		}
	}

	public static JPAQuery addOrderBysOffsetAndLimit(JPAQuery jpaQuery, SearchScope searchScope, final Expression<?> baseExpression) {
		for (Entry<String, String> sortingEntry : searchScope.getSorting().entrySet()) {
			String path = sortingEntry.getKey();
			if (StringUtils.isNotBlank(path)) {
				jpaQuery.orderBy(parseOrderSpecifier(path, sortingEntry.getValue(), baseExpression));
			}
		}

		return jpaQuery
			.offset((searchScope.getCurrentPage() - 1) * searchScope.getCountPerPage())
			.limit(searchScope.getCountPerPage());
	}
}
