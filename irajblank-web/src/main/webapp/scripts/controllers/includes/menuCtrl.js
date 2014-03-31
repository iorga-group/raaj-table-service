'use strict';

angular.module('blank-iraj')
.controller('MenuCtrl', function ($scope, $location, irajBreadcrumbsService) {
	$scope.loadPath = function(path, label) {
		$location.path(path);
		irajBreadcrumbsService.init(path, label);
	};
});