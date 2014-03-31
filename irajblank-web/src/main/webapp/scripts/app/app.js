'use strict';

angular.module('blank-iraj', [
        'ngRoute',
		'paginator',
		'sortoncolumn',
		'iraj-authentication-service',
		'iraj-security-interceptor',
		'iraj-message-interceptor',
		'iraj-message-service',
		'iraj-breadcrumbs-service',
		'iraj-progress-interceptor'
	])
	.config(function(irajProgressInterceptorProvider) {
		irajProgressInterceptorProvider.setDefaultMessage('Chargement en cours...');
	}
);
