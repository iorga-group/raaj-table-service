function router($routeProvider) {
	$routeProvider
		.when('/', {controller:HomeCtrl, templateUrl: 'public/home.html'})
		.when('/user', {controller:UtilisateurCtrl, templateUrl: 'public/views/users/utilisateur.html'})
		.when('/user/:userId', {controller:UtilisateurCtrl, templateUrl: 'public/views/users/ficheUtilisateur.html'})
};