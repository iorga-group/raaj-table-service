'use strict';

angular.module('blank-iraj')
.controller('UserSearchCtrl', function ($scope, $http, $location, irajBreadcrumbsService, irajTableService) {
	/// Action methods ///
	/////////////////////
	
	$scope.search = function(goToPage1) {
		$http.post('api/administration/userSearch/count', $scope.userForm).success(function (count) {
			$scope.count = count;
			if (goToPage1) {
				$scope.tableParams.page(1);
			}
			irajTableService.reloadLazyLoadingTable($scope.tableParams, count);
		});
	};
	
	$scope.selectUser = function(userId){
		$location.path('/administration/userEdit/' + userId);
		irajBreadcrumbsService.push($scope, $location.path());
	};

	/// Initialization ///
	/////////////////////
	$http.get('api/administration/userSearch/init').success(function(data) {
		$scope.profileList = data.profileList;
	});
	
	irajTableService.initLazyLoadingTableWithSearchScope('tableParams', 'userForm.searchScope', $scope, function(tableParams, callbackFn) {
		$http.post('api/administration/userSearch/search', $scope.userForm).success(function (results) {
			callbackFn(results);
		});
	});
	
	// loading from last scope if necessary
	if (irajBreadcrumbsService.shouldLoadFromLastScope()) {
		$scope.userForm = irajBreadcrumbsService.getLast().scope.userForm;
		$scope.tableParams = irajBreadcrumbsService.getLast().scope.tableParams;
		$scope.search(false);
	} else {
		$scope.userForm = {};
	}
	
	irajBreadcrumbsService.setLastLabel('Recherche d\'utilisateur');
});