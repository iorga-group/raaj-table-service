'use strict';
angular.module('blank-iraj').controller('LoginCtrl', [
  '$scope',
  '$rootScope',
  function ($scope, $rootScope) {
    $scope.tryLogin = function (login, password) {
      var digestedPassword = CryptoJS.SHA1(login + '|' + password).toString(CryptoJS.enc.Hex);
      $rootScope.$broadcast('iraj:auth-tryLogin', login, digestedPassword);
    };
    $rootScope.$on('iraj:auth-loginRequired', function () {
      $('#loginModal').modal('show');
      $('#loginModal').on('shown', function () {
        $('#loginModal-login').focus();
      });
    });
    $rootScope.$on('iraj:auth-loginSucced', function () {
      $('#loginModal').modal('hide');
    });
  }
]);