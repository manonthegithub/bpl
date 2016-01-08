var phonecatApp = angular.module('phonecatApp', []);

phonecatApp.controller('PhoneListCtrl', function ($scope) {
  $scope.phones = [
    {'name': 'Nexus S',
     'snippet': 'Fast just got faster with Nexus S.'},
    {'name': 'Motorola XOOM™ with Wi-Fi',
     'snippet': 'The Next, Next Generation tablet.'},
    {'name': 'MOTOROLA XOOM™',
     'snippet': 'The Next, Next Generation tablet.'}
  ];
  $scope.shit = 'helllloooo';
});

phonecatApp.controller('PhoneDetailCtrl', ['$scope', '$http',
    function($scope, $http) {
       /* $http.get('https://instagram.com/irina_pozina/?__a=1').success(function(data) {
            $scope.phone = data;
            $scope.mainImageUrl = data.images[0];
        });*/




    }]);

/*
var phonecatServices = angular.module('phonecatServices', ['ngResource']);

phonecatServices.factory('Phone', ['$resource',
    function($resource){
        return $resource('https://instagram.com/irina_pozina/?__a=1', {}, {
            query: {method:'GET'}
        });
    }]);*/
