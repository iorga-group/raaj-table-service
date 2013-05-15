var module = angular.module('blank-iraj', [
		'$strap.directives',
		'paginator',
		'sortoncolumn',
		'iraj-authentication-service',
		'iraj-security-interceptor',
		'iraj-message-interceptor',
		'iraj-message-service',
		'iraj-breadcrumbs-service'])
	.config(router);
