function router($routeProvider) {
	$routeProvider
		.when('/', {controller:HomeCtrl, templateUrl: 'public/home.html'})
		.when('/searchUser', {controller:SearchUserCtrl, templateUrl: 'public/views/users/searchUser.html'})
		.when('/user', {controller:UserCtrl, templateUrl: 'public/views/users/user.html'})
		.when('/user/:userId', {controller:UserCtrl, templateUrl: 'public/views/users/user.html'})
};