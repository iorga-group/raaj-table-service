'use strict';

angular.module('blank-iraj').controller('UserEditCtrl', [
	'$scope',
    '$routeParams',
    '$http',
    'irajMessageService',
    'irajBreadcrumbsService',
    function ($scope, $routeParams, $http, irajMessageService, irajBreadcrumbsService) {
		/// Action methods ///
		/////////////////////
		$scope.save = function () {
			$http.post('api/administration/userEdit/save', $scope.userEditForm, {
				irajMessagesIdPrefix: 'userEditForm',
				irajClearAllMessages: true
			}).success(function (userId) {
				$scope.userEditForm.userId = userId;
				irajMessageService.displayMessage({
					message: 'L\'utilisateur a bien \xe9t\xe9 enregistr\xe9.',
					type: 'success'
				}, 'userEditForm');
				irajBreadcrumbsService.replace('/administration/userEdit/' + userId, 'Modification d\'un utilisateur');
			});
		};
		/// Initialization ///
		/////////////////////
		$http.get('api/administration/userEdit/init').success(function (data) {
			$scope.profileList = data;
		});
		$scope.userEditForm = {
			userId: 0,
			login: '',
			password: '',
			lastName: '',
			firstName: '',
			profileId: 0,
			active: false
		};
		if ($routeParams.userId) {
			$scope.userEditForm.userId = $routeParams.userId;
		}
		if ($scope.userEditForm.userId !== 0) {
			$http.get('api/administration/userEdit/find/' + $scope.userEditForm.userId).success(function (user) {
				$scope.userEditForm = user;
			});
			irajBreadcrumbsService.setLastLabel('Modification d\'un utilisateur');
		} else {
			irajBreadcrumbsService.setLastLabel('Cr\xe9ation d\'un utilisateur');
		}
	}
]);