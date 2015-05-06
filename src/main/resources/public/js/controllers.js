(function() {
		'use strict';
		angular.module( 'finabay.controllers', [] )
				.controller( 'MainCtrl', [ '$scope', 'Restangular', function( $scope, Restangular ) {
						var api = Restangular.all( 'api/loans' );
						angular.extend( $scope, {
								applicationForm: {},

								addApplication: function() {
										api.post( $scope.applicationForm ).then(
												function() { // success
														$scope.applicationForm = {};
														$scope.refreshApplicationsList();
														$scope.errors = undefined;
												}, function( errors ) { // error
														$scope.errors = errors.data;
												} );
								},
								refreshApplicationsList: function() {
										$scope.applications = api.getList().$object;
								}
						} );
				} ] );
}());