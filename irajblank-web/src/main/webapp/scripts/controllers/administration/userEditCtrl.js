'use strict';

angular.module('blank-iraj')
.controller('UserEditCtrl', function ($scope, $http, $location, irajMessageService) {
	/// Action methods ///
	/////////////////////
	$scope.save = function(){
		$http.post('api/administration/userEdit/save', $scope.userEditForm, {irajMessagesIdPrefix: 'userEditForm', irajClearAllMessages: true})
			.success(function(userId) {
				if (!$scope.userEditForm.userId) {
					$scope.userEditForm.userId = userId;
					$location
						.search({userId: userId})
						.replace();
				}
				irajMessageService.displayMessage({message: 'L\'utilisateur a bien été enregistré.', type: 'success'}, 'userEditForm');
			});
	};

	/// Initialization ///
	/////////////////////
	$http.get('api/administration/userEdit/init').success(function(data) {
		$scope.profileList = data;
	});
	
	$scope.userEditForm = {
		userId : 0,
		login: '',
		password: '',
		lastName : '',
		firstName : '',
		profileId : 0,
		active : false
	};
	
	if (angular.isDefined($location.search().userId)) {
		$scope.userEditForm.userId = $location.search().userId;
	}
	
	if ($scope.userEditForm.userId !== 0) {
		$http.get('api/administration/userEdit/find/' + $scope.userEditForm.userId).success(function(user) {
			$scope.userEditForm = user;
		});
	}
});