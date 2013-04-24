//securityContext = {};

function securityInterceptor($httpProvider) {
	$httpProvider.interceptors.push(function($q/*, $log*/) {
		return {
			'request': function(config) {
				//$log.info(config);
				if (config.url.indexOf('api/') == 0) {
					// this is an api request, let's add the Authorization header
					securityUtils.addAuthorizationHeader('user', securityUtils.digestPassword('user', 'user'), { //FIXME intercept & redirect to login form + call /security/getTime in order to save the time shifting
						method: config.method,
						body: config.transformRequest[0](config.data),
						headers: config.headers,
						resource: config.url.substring(3, config.url.length)
					});
					//$log.info(config);
				}
				return config || $q.when(config);
			}
		}
	})
};