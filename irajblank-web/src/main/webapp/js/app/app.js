var module = angular.module('blank-iraj', [
		'ngRoute',
		'$strap.directives',
		'paginator',
		'sortoncolumn',
		'iraj-authentication-service',
		'iraj-security-interceptor',
		'iraj-message-interceptor',
		'iraj-message-service',
		'iraj-breadcrumbs-service',
		'iraj-progress-interceptor',
		'iraj-table-service'])
	.config(router)
	.config(function(irajProgressInterceptorProvider) {
		irajProgressInterceptorProvider.setDefaultMessage('Chargement en cours...');
	})
	.config(function(irajMessageServiceProvider) {
		irajMessageServiceProvider.setBootstrapVersion('3.x');
	})
;
