'use strict';

angular.module('blank-iraj').config(function ($routeProvider) {
	$routeProvider
	.when('/', {
		controller: 'HomeCtrl',
		templateUrl: 'templates/views/home.html'
	}).when('/administration/userSearch', {
		controller: 'UserSearchCtrl',
		templateUrl: 'templates/views/administration/userSearch.html',
		reloadOnSearch: false
	}).when('/administration/userEdit', {
		controller: 'UserEditCtrl',
		templateUrl: 'templates/views/administration/userEdit.html',
		reloadOnSearch: false
	});
});