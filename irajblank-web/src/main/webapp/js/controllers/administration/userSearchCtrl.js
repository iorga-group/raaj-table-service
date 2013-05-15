function UserSearchCtrl($scope, $http, $location, irajBreadcrumbsService) {
	/// Action methods ///
	/////////////////////
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
	}
	
	$scope.selectUser = function(userId){		
		$location.path('/administration/userEdit/' + userId);
		irajBreadcrumbsService.push($scope, $location.path());
	}

	/// Initialization ///
	/////////////////////
	$http.get('api/administration/userSearch/init').success(function(data, status, headers, config) {
		$scope.profileList = data.profileList;
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