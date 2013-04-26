var module = angular.module('blank-iraj', ['$strap.directives', 'paginator', 'sortoncolumn', 'security-interceptor'])
	.config(router);
//	.config(securityInterceptor);

var paginator = angular.module('paginator', []);
paginator.directive('paginator', function () {
  return {
    priority: 0,
    restrict: 'A',
    scope: {searchform : '@',
    		searchmethod : '@'},
    templateUrl: "public/module/paginator.html",
    replace: true,
    compile: function compile(tElement, tAttrs, transclude) {
      return {
        pre: function preLink(scope, iElement, iAttrs, controller) {
          scope.pageSizeList = [10, 20, 30, 40, 50, 60, 70, 80, 90, 100];
          scope.nbPageVisible = 5;
          scope.numerosPage = [];
          
          scope.majTabNumPage = function(){
        	  scope.numerosPage = [];

        	  var startIndex = 1;
        	  var endIndex = scope.paginator.nbPages;
        	  
        	  if (scope.paginator.nbPages > scope.nbPageVisible){

	        	  if (scope.paginator.currentPage-2 > 1){
	        		  startIndex = scope.paginator.currentPage-2;
	        	  }
	
	        	  var endIndex = scope.paginator.currentPage + 2;
	
	        	  if (endIndex > scope.paginator.nbPages){
	        		  endIndex = scope.paginator.nbPages;
	        	  }
	
	        	  if (endIndex - startIndex < scope.nbPageVisible-1){
	        		  if (endIndex == scope.paginator.nbPages){
	        			  startIndex -= scope.nbPageVisible - (endIndex - startIndex) - 1;
	        		  }else{
	        			  endIndex += scope.nbPageVisible - (endIndex - startIndex) - 1;
	        		  }
	        	  }
        	  }

        	  for (var i = startIndex; i <= endIndex; i++) {
        		  scope.numerosPage.push(i);
        	  }
          };
          
          scope.paginator = {
            pageSize: 10,
            nbPages : 0,
      		currentPage : 1
          };

          scope.goToPage = function(numPage){
          	if (scope.paginator.currentPage != numPage){
      	    	scope.paginator.currentPage = numPage;
      	    	
      	    	//On met à jour la valeur currentPage du form concerné et on lance la recherche.
      	    	scope.$parent.$eval(scope.searchform).currentPage = numPage;
      	    	scope.$parent.$eval(scope.searchmethod);
      	    	
      	    	scope.majTabNumPage();
          	}
          }
          
          scope.goToNextPage = function(){
          	if (scope.paginator.currentPage + scope.nbPageVisible < scope.paginator.nbPages){
          		if (scope.paginator.currentPage < 3){
          			scope.paginator.currentPage = 2 + scope.nbPageVisible;
          		}else{
          			scope.paginator.currentPage += scope.nbPageVisible;
          		}
          		
          		if (scope.paginator.currentPage > scope.paginator.nbPages){
          			scope.paginator.currentPage = scope.paginator.nbPages;
          		}
          	}else{
          		scope.paginator.currentPage = scope.paginator.nbPages-2;
          	}
          	
          	//On met à jour la valeur currentPage du form concerné et on lance la recherche.
  	    	scope.$parent.$eval(scope.searchform).currentPage = numPage;
          	scope.$parent.$eval(scope.searchmethod);
          	
          	scope.majTabNumPage();
          }
          
          scope.goToPreviousPage = function(numPage){
          	if (scope.paginator.currentPage - scope.nbPageVisible > 1){
          		if (scope.paginator.currentPage > scope.paginator.nbPages-2){
          			scope.paginator.currentPage =  scope.paginator.nbPages-2-scope.nbPageVisible;
          		}else{
          			scope.paginator.currentPage -= scope.nbPageVisible;
          		}
          	}else{
          		scope.paginator.currentPage = 3;
          	}
          	
          	//On met à jour la valeur currentPage du form concerné et on lance la recherche.
  	    	scope.$parent.$eval(scope.searchform).currentPage = scope.paginator.currentPage;
          	scope.$parent.$eval(scope.searchmethod);
          	scope.majTabNumPage();
          }
          
          scope.goToLastPage = function(){
        	  scope.paginator.currentPage = scope.paginator.nbPages;
        	  
        	  //On met à jour la valeur currentPage du form concerné et on lance la recherche.
        	  scope.$parent.$eval(scope.searchform).currentPage = scope.paginator.currentPage;
        	  scope.$parent.$eval(scope.searchmethod);
        	  scope.majTabNumPage();
          }

          scope.goToFirstPage = function(){
        	  scope.paginator.currentPage = 1;
        	  
        	  //On met à jour la valeur currentPage du form concerné et on lance la recherche.
        	  scope.$parent.$eval(scope.searchform).currentPage = scope.paginator.currentPage;
        	  scope.$parent.$eval(scope.searchmethod);
        	  scope.majTabNumPage();
          }
          
          scope.showNextButtons = function(){
        	  if (scope.paginator.nbPages <= scope.nbPageVisible){
        		  return false;
        	  }
        	  if (scope.paginator.currentPage > 3){
        		  return true;
        	  }
        	  return false;
          }
          
          scope.showPreviousButtons = function(){
        	  if (scope.paginator.nbPages <= scope.nbPageVisible){
        		  return false;
        	  }
        	  if (scope.paginator.currentPage <= (scope.paginator.nbPages-3)){
        		  return true;
        	  }
        	  return false;
          }
          
          scope.$watch('paginator.pageSize', function(newValue, oldValue) { 
            if (newValue != oldValue) {
            	scope.$parent.$eval(scope.searchform).pageSize = newValue;
            	scope.goToFirstPage();
            }
          });
          
          scope.$watch('paginator.nbPages', function(newValue, oldValue) { 
              if (newValue != oldValue) {
                scope.majTabNumPage();
              }
            });

          // ---- Functions and properties available in parent scope -----

          scope.$parent.firstPage = function () {
            scope.goToFirstPage();
          };
          
          scope.$parent.paginator = scope.paginator;
        },
        post: function postLink(scope, iElement, iAttrs, controller) {}
      };
    }
  };
});

var sortoncolumn = angular.module('sortoncolumn', []);
sortoncolumn.directive('sortoncolumn', function () {
  return {
    priority: 0,
    restrict: 'A',
    scope: {searchform: '@',
    		searchmethod : '@',
    		column : '@sortoncolumn'},
    template: 	"<a href='' ng-click='searchByColumn(\"asc\")'><i class=\"icon-arrow-up\"></i></a>" +
    			"<a href='' ng-click='searchByColumn(\"desc\")'><i class=\"icon-arrow-down\"></i></a>",
    replace: false,
    compile: function compile(tElement, tAttrs, transclude) {
      return {
        pre: function preLink(scope, iElement, iAttrs, controller) {
        	scope.searchByColumn = function(directionParam){
        		scope.$parent.$eval(scope.searchform).orderByPath = scope.column;
        		scope.$parent.$eval(scope.searchform).orderByDirection = directionParam;
           	  	scope.$parent.$eval(scope.searchmethod);
            }
        },
        post: function postLink(scope, iElement, iAttrs, controller) {}
      };
    }
  };
});