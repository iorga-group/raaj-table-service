'use strict';

angular.module('sortoncolumn', [])
	.directive('sortoncolumn', function () {
		return {
			priority: 0,
			restrict: 'A',
			scope: {searchform: '@',
					searchmethod : '@',
					column : '@sortoncolumn'},
			template: '<a href="" ng-click="searchByColumn(\'asc\')"><i class="icon-arrow-up"></i></a>' +	'<a href="" ng-click="searchByColumn(\'desc\')"><i class="icon-arrow-down"></i></a>',
			replace: false,
			compile: function compile() {
				return {
					pre: function preLink(scope) {
						scope.searchByColumn = function(directionParam){
							scope.$parent.$eval(scope.searchform).orderByPath = scope.column;
							scope.$parent.$eval(scope.searchform).orderByDirection = directionParam;
							scope.$parent.$eval(scope.searchmethod);
						};
					},
					post: function postLink() {}
				};
			}
		};
	})
;