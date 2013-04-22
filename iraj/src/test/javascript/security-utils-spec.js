describe('the securityUtils object', function() {
	// beforeEach(function () {});
	// afterEach(function () {});
	
	// Specs
	it('must add the authentication header to a simple request', function() {
		var request = {
			method: 'GET',
			body: 'Body Test',
			headers: {
				'Content-Type': 'text/plain',
				'Date': 'Mon, 22 Apr 2013 00:00:00 GMT',
				'X-IRAJ-Date': 'Thu, 01 Jan 1970 00:00:00 GMT'
			},
			resource: '/'
		};
		securityUtils.addAuthorizationHeader('5YP9Z3DVCAHVDZPC0617VT91D', 'iLKJ8zhzU/5eEZFKeQ5bP+piXQ/JQr4+QKbORZP0', request);
		expect(request.headers['Authorization']).toBe('IWS 5YP9Z3DVCAHVDZPC0617VT91D:fWUiX2xF1+oSDIv7m+3cbo8Ve88=');
	})
});