function UserEditCtrl($scope, $routeParams, $http) {
	
	$http.get('api/administration/userEdit/init').success(function(data, status, headers, config) {
		$scope.profileList = data;
	});
	
	$scope.userForm = {
			userId : 0,
			login: "",
			password: "",
			nom : "",
			firstName : "",
			profileId : 0,
			active : false
			
	}
	
	if ($routeParams.userId){
		$scope.userForm.userId = $routeParams.userId;
	}
	
	if ($scope.userForm.userId != 0){
		$http.get('api/administration/userEdit/find/' + $scope.userForm.userId).success(function(user, status, headers, config) {
			$scope.userForm = user;
		});
	}
	
	$scope.saveUserForm = function(){
		$http.post('api/administration/userEdit/save', $scope.userForm)
		.success(function(userId, status, headers, config){
			$scope.userForm.userId = userId;
			$scope.messageInfo = "L'utilisateur a bien été enregistré."
		})
	}
}