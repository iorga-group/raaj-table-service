function UtilisateurCtrl($scope, $http, $location) {
	
	$scope.showUsers = function() {
		$http.get('api/user/findAll').success(function(listUser, status, headers, config) {
			$scope.users = listUser;
		});
	};
	
	$scope.userForm = {
		userFilter : {currentPage : 1}
	};
	
	$scope.searchUser = function(newSearch){
		if (newSearch){
			$scope.userForm.userFilter.currentPage = 1;
		}else{
			$scope.userForm.userFilter.currentPage = $scope.paginator.currentPage;
		}
		$http({
		    url: 'api/user/search',
		    method: "POST",
		    data: $scope.userForm
		})
		.success(function(data, status, headers, config) {
					$scope.users = data.userSearchTemplate.listUser;
					$scope.paginator.nbPages = data.userSearchTemplate.nbPages;
					$scope.nbResults = data.userSearchTemplate.nbResults;
				});
	}
	
	$scope.findUser = function(userId){		
		$location.path('/daedalus/' + $scope.daedalusId + '/' + $scope.characterPrivateId);
	}
	
}