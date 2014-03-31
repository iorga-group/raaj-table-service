function UserSearchCtrl($scope, $http, $location, irajBreadcrumbsService, irajTableService) {
	/// Action methods ///
	/////////////////////
	
	/*
	$scope.showUsers = function() {
		$http.get('api/administration/userSearch/findAll').success(function(listUser, status, headers, config) {
			$scope.users = listUser;
		});
	};
	
	$scope.searchUser = function(newSearch){		
		if (newSearch){
			$scope.userForm.currentPage = 1;
		}
		$http({
		    url: 'api/administration/userSearch/search',
		    method: "POST",
		    data: $scope.userForm
		})
		.success(function(data, status, headers, config) {
					$scope.users = data.listUser;
					$scope.paginator.nbPages = data.nbPages;
					$scope.nbResults = data.nbResults;
				});
	};*/
	
	$scope.search = function() {
		$http.post('api/administration/userSearch/count', $scope.userForm).success(function (count) {
			$scope.count = count;
		});
	};
	
	$scope.selectUser = function(userId){		
		$location.path('/administration/userEdit/' + userId);
		irajBreadcrumbsService.push($scope, $location.path());
	};

	/// Initialization ///
	/////////////////////
	$http.get('api/administration/userSearch/init').success(function(data, status, headers, config) {
		$scope.profileList = data.profileList;
	});
	
	irajTableService.initLazyLoadingTable('tableParams', 'count', $scope, function(tableParams, callbackFn) {
		$scope.userForm.searchScope = {
			currentPage: tableParams.page,
			countPerPage: tableParams.count,
			sorting: tableParams.sorting()
		};
		
		$http.post('api/administration/userSearch/search', $scope.userForm).success(function (results) {
			callbackFn(results);
		});
	});
	
	// loading from last scope if necessary
	if (irajBreadcrumbsService.shouldLoadFromLastScope()) {
		$scope.userForm = irajBreadcrumbsService.getLast().scope.userForm;
		$scope.searchUser();
	} else {
		$scope.userForm = {	
			currentPage : 1,
			pageSize : 10,
			orderByPath : "",
			orderByDirection : ""
		};
	}
	
	irajBreadcrumbsService.setLastLabel('Recherche d\'utilisateur');
}