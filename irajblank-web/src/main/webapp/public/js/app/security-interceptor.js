// based on https://github.com/witoldsz/angular-http-auth/blob/master/src/http-auth-interceptor.js
'use strict';

angular.module('security-interceptor', [])
	.factory('authService', function($injector, $rootScope) {
		var $http;	// Service initialized later because of circular dependency problem.
		
		var queryBuffer = [],
			retryHttpRequest = function(config, deferred) {
				authObject.addAuthorizationHeader(config);
				return deferred.resolve(config);
			};
		var authObject = {
			tryLogin: function(login, digestedPassword) {
				authObject.login = login;
				authObject.digestedPassword = digestedPassword;
				$http = $http || $injector.get('$http');	// Lazy inject
				$http.get('api/security/getTime', {authenticating: true})
					.success(function(data, status, headers, config) {
						// data : server time
						authObject.authenticated = true;
						authObject.timeShifting = new Date().getTime() - data;
						$rootScope.$broadcast('event:auth-loginSucced', data, status, headers, config);
						authObject.retryAllQueries();
					})
					.error(function(data, status, headers, config) {
						$rootScope = $rootScope || $injector.get('$rootScope');	// Lazy inject
						$rootScope.$broadcast('event:auth-loginFailed', data, status, headers, config);
					});
			},
			appendQuery: function(config, deferred) {
				queryBuffer.push({
					config: config,
					deferred: deferred
				});
			},
			retryAllQueries: function() {
				for (var i = 0; i < queryBuffer.length; ++i) {
					retryHttpRequest(queryBuffer[i].config, queryBuffer[i].deferred);
				}
			},
			addAuthorizationHeader: function(config) {
				securityUtils.addAuthorizationHeader(authObject.login, authObject.digestedPassword, { //FIXME intercept & redirect to login form + call /security/getTime in order to save the time shifting
					method: config.method,
					body: config.transformRequest[0](config.data),
					headers: config.headers,
					resource: config.url.substring(3, config.url.length)
				});
			},
			login: null,
			digestedPassword: null,
			authenticated: false,
			timeShifting: 0
		};
		$rootScope.$on('event:auth-tryLogin', function(event, login, digestedPassword) {
			authObject.tryLogin(login, digestedPassword);
		});
		return authObject;
	})
	.config(function ($httpProvider) {
		$httpProvider.interceptors.push(function($q, $rootScope, authService) {
			return {
				'request': function(config) {
					//$log.info(config);
					if (config.url.indexOf('api/') == 0) {
						if (!authService.authenticated && !config.authenticating) {
							var deferred = $q.defer();
							authService.appendQuery(config, deferred);
							$rootScope.$broadcast('event:auth-loginRequired');
							return deferred.promise;
						} else {
							// this is an api request, let's add the Authorization header
							authService.addAuthorizationHeader(config);
						}
					}
					return config;
				}
			}
		})
	})
;
