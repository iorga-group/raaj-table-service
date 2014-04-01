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
	.factory('irajTableService', function(ngTableParams, $filter, $parse, $timeout) {
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
		
		irajTableService.initLazyLoadingTable = function(tableParamsName, scope, loadFn) {
			var tableParams;
			
			if (arguments.length == 4) {
				// function(tableParamsName, scope, tableParams, loadFn)
				tableParams = arguments[2];
				loadFn = arguments[3];
			}
			
			var tableParamsP = $parse(tableParamsName);
			if (angular.isDefined(tableParams)) {
				// assign the given tableParams if given
				tableParamsP.assign(scope, tableParams);
			}
			if (tableParamsP(scope) == null) {
				// table params not defined yet, let's define a new one
				tableParams = new ngTableParams({
					page: 1,
					count: 25,
					sorting: {}
				});
				tableParamsP.assign(scope, tableParams);
			}
			
			// define the getData function which will call the given loadFn
			tableParams.settings({getData: function(defer, params) {
				if (tableParams.total()) {
					loadFn(tableParams, function(data) {
						defer.resolve(data);
					});
				}
			}});
		}
		
		irajTableService.reloadLazyLoadingTable = function(tableParams, count) {
			tableParams.total(count);
			tableParams.reload();
		}
		
		irajTableService.initLazyLoadingTableWithSearchScope = function(tableParamsName, searchScopeName, scope, loadFn) {
			var searchScopeLoadFn = function(tableParams, callbackFn) {
				// sync tableParams variables to searchScope
				var searchScope = {
						currentPage: tableParams.page(),
						countPerPage: tableParams.count(),
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
			};
			
			var tableParams;
			
			if (arguments.length == 5) {
				// function(tableParamsName, searchScopeName, scope, tableParams, loadFn)
				tableParams = arguments[3];
				loadFn = arguments[4];

				irajTableService.initLazyLoadingTable(tableParamsName, scope, tableParams, searchScopeLoadFn);
			} else {
				irajTableService.initLazyLoadingTable(tableParamsName, scope, searchScopeLoadFn);
			}
		}
		
		return irajTableService;
	})
;
