var module = angular.module('blank-iraj', [
		'$strap.directives',
		'paginator',
		'sortoncolumn',
		'iraj-authentication-service',
		'iraj-security-interceptor',
		'iraj-message-interceptor',
		'iraj-message-service',
		'iraj-breadcrumbs-service',
		'iraj-progress-interceptor'])
	.config(router)
	.config(function(irajProgressInterceptorProvider) {
		irajProgressInterceptorProvider.setDefaultMessage('Chargement en cours...');
	});
