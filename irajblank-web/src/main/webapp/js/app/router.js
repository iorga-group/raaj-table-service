function router($routeProvider) {
	$routeProvider
		.when('/', {controller:HomeCtrl, templateUrl: 'templates/views/home.html'})
		.when('/administration/userSearch', {controller:UserSearchCtrl, templateUrl: 'templates/views/administration/userSearch.html'})
		.when('/administration/userEdit', {controller:UserEditCtrl, templateUrl: 'templates/views/administration/userEdit.html'})
		.when('/administration/userEdit/:userId', {controller:UserEditCtrl, templateUrl: 'templates/views/administration/userEdit.html'})
};