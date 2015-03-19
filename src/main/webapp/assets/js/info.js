angular.module('info', ['ngResource']).
    factory('Info', function ($resource) {
        return $resource('info');
//    });
    })
    .factory('Instance', function ($resource){
        return $resource('instance');
    }).factory('SessionInfo', function($resource){
        return $resource('albums/sessionContent');});

function InfoController($scope, Info, Instance, SessionInfo) {
    $scope.info = Info.get();
    $scope.instance = Instance.get();
    $scope.sessionInfo = SessionInfo.get();

}

