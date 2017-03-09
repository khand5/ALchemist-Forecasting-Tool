/*
  The Starter module is the main module of the client which:
    - processes data for visualization
    - queries server
    - routes views
*/
angular.module('starter', ['ionic', 'starter.controllers', 'starter.services', 'highcharts-ng'])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default
    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);
    }

    if (window.StatusBar) {
      // required by org.apache.cordova.statusbar
      StatusBar.styleDefault();
    }
  });
})

/*
  Define all the states, and a fallback state if
  the application is in an invalid state.

  All state controllers can be found in controllers.js
*/
.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider

  // setup an abstract state for the tabs directive
  .state('tab', {
    url: '/tab',
    abstract: true,
    templateUrl: 'templates/tabs.html'
  })

  // Each tab has its own nav history stack:

  .state('tab.server', {
    url: '/server',
    views: {
      'tab-server': {
        templateUrl: 'templates/tab-server.html',
        controller: 'ServerController'
      }
    }
  })

  .state('tab.search', {
    url: '/search',
    views: {
      'tab-search': {
        templateUrl: 'templates/tab-search.html',
        controller: 'SearchController'
      }
    }
  })

  .state('tab.search-detail', {
    url: '/search/results',
    views: {
      'tab-search': {
        templateUrl: 'templates/tab-search-results.html',
        controller: 'SearchResultsController'
      }
    }
  })

  $urlRouterProvider.otherwise('/tab/server');
})
