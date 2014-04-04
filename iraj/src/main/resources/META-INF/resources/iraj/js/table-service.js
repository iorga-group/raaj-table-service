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
'use strict';

angular.module('iraj-table-service', ['ngTable'])
	.factory('irajTableService', function(ngTableParams, $filter, $parse, $location, $route) {
		var irajTableService = {};
		
		/*
		irajTableService.getDataFn = function(tableParamsName, dataExpression, scope) {
			return function($defer, params) {
				var data = scope.$eval(dataExpression);
				var orderedData = params.sorting() ? 
						$filter('orderBy')(data, params.orderBy()) :
						data;

				$defer.resolve(orderedData.slice(
					(params.page() -1) * params.count(),
					params.page() * params.count()
				));
			}
		}
		
		irajTableService.sortTable = function(tableParamsName, dataExpression, orderedDataExpression, scope) {
			var data = scope.$eval(dataExpression);
			var tableParams = scope.$eval(tableParamsName);
			// see http://esvit.github.io/ng-table/#!/demo3
			// use build-in angular filter
			var orderedData = tableParams.sorting ? 
				$filter('orderBy')(data, tableParams.orderBy()) :
				data;
	 
			// slice array data on pages
			var list;
			if (orderedData) {
				list = orderedData.slice(
					(tableParams.page - 1) * tableParams.count,
					tableParams.page * tableParams.count
				);
			}
			
			$parse(orderedDataExpression).assign(scope, list);
//			$scope.$eval(orderedDataExpression+'=') = list;
		}
		
		irajTableService.initTable = function(tableParamsName, dataExpression, scope, displayedRowsCount) {
			scope.$watch(dataExpression, function(data) {
				if (data) {
					var tableParams = scope.$eval(tableParamsName);
					tableParams.total = data.length;
					tableParams.page = 1;
				}
			});
			$parse(tableParamsName).assign(scope, new ngTableParams({
				page: 1,
				count: displayedRowsCount ? displayedRowsCount : 25
			}, {
				getData: irajTableService.getDataFn(tableParamsName, dataExpression, scope)
			}));
//			scope.$watch(tableParamsName, function() {
//				irajTableService.sortTable(tableParamsName, dataExpression, orderedDataExpression, scope);
//			});
//			scope.$watch(dataExpression, function(data) {
//				if (data) {
//					var tableParams = scope.$eval(tableParamsName);
//					tableParams.total = data.length;
//					tableParams.page = 1;
//					irajTableService.sortTable(tableParamsName, dataExpression, orderedDataExpression, scope);
//				}
//			});
//			$parse(tableParamsName).assign(scope, new ngTableParams({count: displayedRowsCount ? displayedRowsCount : 25}));
		}

		irajTableService.getDataFn = function(tableParamsName, dataExpression, scope) {
			return function($defer, params) {
				var data = scope.$eval(dataExpression);
				var orderedData = params.sorting() ? 
						$filter('orderBy')(data, params.orderBy()) :
						data;

				$defer.resolve(orderedData.slice(
					(params.page() -1) * params.count(),
					params.page() * params.count()
				));
			}
		}
		*/
		
		irajTableService.createNewNgTableParams = function() {
			return new ngTableParams({
				page: 1,
				count: 25,
				sorting: {}
			});
		};
		
		irajTableService.getOrCreateTableParams = function(tableParamsName, scope) {
			var tableParamsP = $parse(tableParamsName),
				tableParams = tableParamsP(scope);
			
			if (angular.isUndefined(tableParams)) {
				// table params not defined yet, let's define a new one
				tableParams = irajTableService.createNewNgTableParams();
				tableParamsP.assign(scope, tableParams);
			}
			
			return tableParams;
		}
		
		irajTableService.initLazyLoadingTable = function(tableParamsName, scope, loadFn) {
			var tableParams = irajTableService.getOrCreateTableParams(tableParamsName, scope);
			
			// define the getData function which will call the given loadFn
			tableParams.settings({getData: function(defer, params) {
				if (tableParams.total()) {
					loadFn(tableParams, function(data) {
						defer.resolve(data);
					});
				}
			}});
		};
		
		irajTableService.reloadLazyLoadingTable = function(tableParams, count) {
			function reload(count) {
				tableParams.total(count);
				tableParams.reload();
			}
			if (angular.isUndefined(count)) {
				var countFn = tableParams.countFn;
				if (angular.isUndefined(countFn)) {
					throw 'Must define a countFn in the tableParams if not passing count param for reloadLazyLoadingTable';
				}
				countFn(tableParams, function(count) {
					reload(count);
				})
			} else {
				reload(count);
			}
		};
		
		irajTableService.initLazyLoadingTableWithSearchScope = function(tableParamsName, searchScopeName, scope, loadFn) {
			irajTableService.initLazyLoadingTable(tableParamsName, scope, function(tableParams, callbackFn) {
				// sync tableParams variables to searchScope
				var searchScope = {
						countPerPage: tableParams.count(),
						currentPage: tableParams.page(),
						sorting: tableParams.sorting()
					},
					searchScopeVarP = $parse(searchScopeName),
					searchScopeVar = searchScopeVarP(scope);
				
				if (angular.isDefined(searchScopeVar)) {
					angular.extend(searchScopeVar, searchScope);
				} else {
					searchScopeVarP.assign(scope, searchScope);
				}
				loadFn(tableParams, callbackFn);
			});
		};
		
		/**
		 * The object corresponding to the given formName must contain a field "searchScope" which contains the searchScope.
		 * The object corresponding to the given formName will be remembered in the $location
		 */
		irajTableService.initLazyLoadingTableWithSearchScopeAndHistory = function(tableParamsName, formName, scope, loadFn, countFn) {
			var formValueStr = $location.search()[formName],
				searchScopeName = formName + ".searchScope",
				tableParams = irajTableService.getOrCreateTableParams(tableParamsName, scope),
				mustReload = angular.isDefined(formValueStr);
			
			if (mustReload) {
				scope[formName] = angular.fromJson(formValueStr);
				// set it to the tableParams
				var searchScopeVar = $parse(searchScopeName)(scope);
				
				if (angular.isDefined(searchScopeVar)) {
					tableParams.count(searchScopeVar.countPerPage);
					tableParams.page(searchScopeVar.currentPage);
					tableParams.sorting(searchScopeVar.sorting);
				}
			} else if (angular.isUndefined(scope[formName])){
				// formValue not in the URL & the form is not defined, let's set a new one.
				scope[formName] = {};
			}
			irajTableService.initLazyLoadingTableWithSearchScope(tableParamsName, searchScopeName, scope, function(tableParams, callbackFn) {
				var searchVar = {};
				searchVar[formName] = angular.toJson(scope[formName]);
				$location.search(searchVar);
				if (angular.isDefined($route.current.reloadOnSearch) && !$route.current.reloadOnSearch) {
					$location.replace();
				}
				loadFn(tableParams, callbackFn);
			});
			
			tableParams.countFn = countFn;
			
			if (mustReload) {
				irajTableService.reloadLazyLoadingTable(tableParams);
			}
		};
		
		return irajTableService;
	})
;
