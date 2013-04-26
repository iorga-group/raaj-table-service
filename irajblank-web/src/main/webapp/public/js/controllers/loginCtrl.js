function LoginCtrl($scope, $rootScope) {
	$scope.tryLogin = function(login, password) {
		var digestedPassword = CryptoJS.SHA1(login+'|'+password).toString(CryptoJS.enc.Hex);
		$rootScope.$broadcast('event:auth-tryLogin' , login, digestedPassword);
	};
	$rootScope.$on('event:auth-loginRequired', function() {
		$('#loginModal').modal('show');
	});
	$rootScope.$on('event:auth-loginSucced', function() {
		$('#loginModal').modal('hide');
	})
}