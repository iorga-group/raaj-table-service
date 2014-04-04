'use strict';

angular.module('blank-iraj', [
	'ngRoute',
	'iraj-authentication-service',
	'iraj-security-interceptor',
	'iraj-message-interceptor',
	'iraj-message-service',
	'iraj-breadcrumbs-service',
	'iraj-progress-interceptor',
	'iraj-table-service'
]).config([
	'irajProgressInterceptorProvider',
	function (irajProgressInterceptorProvider) {
		irajProgressInterceptorProvider.setDefaultMessage('Chargement en cours...');
	}
]).config([
	'irajMessageServiceProvider',
	function (irajMessageServiceProvider) {
		irajMessageServiceProvider.setBootstrapVersion('3.x');
	}
]);