// disable this to turn off log messages
var DEBUG = true;

/*
  This is the Services submodule of the main Starter module.
   - It handles server queries and data management.
   - It also processes results and readies them for visualization.
*/
angular.module('starter.services', [])

.factory('Alchemist', function($http) {
  var root = 'http://localhost:4567'

  /*
    Server end point functions
  */

  function get(category, search) {
    if(DEBUG) console.log("Requesting \t" + root + '/get/' + category + '/' + search)
    return $http.get(root + '/api/get/' + category + '/' + search)
  }

  function ping() {
    if(DEBUG) console.log("Pinging.")
    return $http.get(root + '/api/ping')
  }

  function generateSeries(searchType, searchFor, xColumn, yColumn) {
    if(DEBUG) console.log("Requesting \t" + root + '/api/search/' + searchType + '/' + searchFor + '/generate/' + xColumn + '/' + yColumn)
    return $http.get(root + '/api/search/'+searchType+'/'+searchFor+'/generate/'+xColumn+'/'+yColumn)
  }

  /*
    Server settings
  */

  function getIP() {
    return root
  }

  function setIP(ip) {
    if(DEBUG) console.log('Setting root to ' + ip)
    root = ip
  }

  var api = {
    get,
    ping,
    getIP,
    setIP,
    generateSeries
  }

  return api
})

.service('Grants', function(Alchemist) {
  // the grant data
  var grants = []

  // current search type selected
  var searchType = 'a'

  // current search string
  var searchFor = '10000'

  // processed series data for the visual graphs (could be empty)
  var seriesData = [[], [], [], [], [], [], [], [], []]

  // titles for each visual graph
  var seriesNames = ['', '', '', '', '', '', '', '', '']

  // sets the series data for a specific graph
  // @param data
  //    is the data returned by the server, which is an array of objects (shown below)
  //    [{
  //      'name': string,
  //      'data': number[]
  //    }]
  function setSeriesData(id, data) {
    series[id].series = data
    seriesData[id] = data
  }

  // sets the title for a specific graph
  function setSeriesName(id, name) {
    series[id].title.text = name
    seriesNames[id] = name
  }

  // returns an array of objects, as defined below:
  //    [{
  //      'name': string,
  //      'data': number[]
  //    }]
  function getSeriesData(id) {
    return seriesData[id];
  }

  // returns the title of a graph
  function getSeriesName(id) {
    return seriesNames[id];
  }

  var provinceToMap = {
    "Ontario":'ca-on',
  	"Quebec":'ca-qc',
  	"Nova Scotia":'ca-ns',
  	"Prince Edward Island":'ca-pe',
  	"Manitoba":'ca-mb',
  	"Alberta":'ca-ab',
  	"New Brunswick":'ca-nb',
  	"Newfoundland and Labrador":'ca-nl',
  	"Saskatchewan":'ca-sk',
  	"British Columbia":'ca-bc',
  	"Northwest Territories":'ca-nt',
  	"Yukon":'ca-yt',
  	"Nunavut":'ca-nu',
  }

  function makeMap(series) {
    resetMapValues()

    for(var i = 0; i < series.length; i++) {
      var name = series[i]['name']
      var data = series[i]['data']

      var mapKey = provinceToMap[name]
      var mapValue = data

      setMapValue(mapKey, mapValue)
    }
  }

  function setMapValue(key, value) {
    var data = series[0].series[0]['data']
    for(var i = 0; i < data.length; i++) {
      if(data[i]['hc-key'] == key) {
        data[i]['value'] = value
        break
      }
    }
  }

  // resets all map data
  function resetMapValues() {
    series[0].series[0]['data'] = data
  }

  // hardcoded map data
  var data = [
    {
        "hc-key": "ca-5682",
        "value": 0
    },
    {
        "hc-key": "ca-bc",
        "value": 0
    },
    {
        "hc-key": "ca-nu",
        "value": 0
    },
    {
        "hc-key": "ca-nt",
        "value": 0
    },
    {
        "hc-key": "ca-ab",
        "value": 0
    },
    {
        "hc-key": "ca-nl",
        "value": 0
    },
    {
        "hc-key": "ca-sk",
        "value": 0
    },
    {
        "hc-key": "ca-mb",
        "value": 0
    },
    {
        "hc-key": "ca-qc",
        "value": 0
    },
    {
        "hc-key": "ca-on",
        "value": 0
    },
    {
        "hc-key": "ca-nb",
        "value": 0
    },
    {
        "hc-key": "ca-ns",
        "value": 0
    },
    {
        "hc-key": "ca-pe",
        "value": 0
    },
    {
        "hc-key": "ca-yt",
        "value": 0
    }
  ]

  // hard coded options for the graphs
  var series = [
    {
      options: {
          legend: {
              enabled: false
          },
          plotOptions: {
              map: {
                  mapData: Highcharts.maps['countries/ca/ca-all'],
                  joinBy: ['name']
              }
          },
          colorAxis: {
            min: 0,
            type: 'linear',
            minColor: '#EEEEFF',
            maxColor: '#000022',
          },
          mapNavigation: {
            enabled: true
          },
        },
        chartType: 'map',
        title: {
            text: 'Funds Distribution'
        },
        series: [{
          data : data,
          mapData: Highcharts.maps['countries/ca/ca-all'],
          joinBy: 'hc-key',
          name: 'Map Data',
          states: {
              hover: {
                  color: '#BADA55'
              }
          },
          dataLabels: {
              enabled: true,
              format: '{point.name}'
          }
        }],
    },
    {
      options: {  chart: { type: 'bar' },
                  xAxis: { categories: ["<b>Info</b>"], visible: false },
                  yAxis: { visible: false },
                  tooltip: { pointFormat: '<span>{series.name}</span>: {point.y} <b>({point.percentage:.0f}%)</b><br />' },
                  plotOptions: { bar: { stacking: 'percent' } } },
      size: { height: 200 },
      series: getSeriesData(1),
      title: { text: getSeriesName(1) },
    },
    {
      options: {  chart: { type: 'bar' },
                  xAxis: { categories: ["<b>Info</b>"], visible: false },
                  yAxis: { visible: false },
                  tooltip: { pointFormat: '<span>{series.name}</span>: {point.y} <b>({point.percentage:.0f}%)</b><br />' },
                  plotOptions: { bar: { stacking: 'percent' } } },
      size: { height: 200 },
      series: getSeriesData(2),
      title: { text: getSeriesName(2) },
    },
    {
      options: {  chart: { type: 'bar' },
                  xAxis: { categories: ["<b>Info</b>"], visible: false },
                  yAxis: { visible: false },
                  tooltip: { pointFormat: '<span>{series.name}</span>: {point.y} <b>({point.percentage:.0f}%)</b><br />' },
                  plotOptions: { bar: { stacking: 'percent' } } },
      size: { height: 200 },
      series: getSeriesData(3),
      title: { text: getSeriesName(3) },
    },
    {
      options: {  chart: { type: 'bar' },
                  xAxis: { categories: ["<b>Info</b>"], visible: false },
                  yAxis: { visible: false },
                  tooltip: { pointFormat: '<span>{series.name}</span>: {point.y} <b>({point.percentage:.0f}%)</b><br />' },
                  plotOptions: { bar: { stacking: 'percent' } } },
      size: { height: 200 },
      series: getSeriesData(4),
      title: { text: getSeriesName(4) },
    },
    {
      options: {  chart: { type: 'bar' },
                  xAxis: { categories: ["<b>Info</b>"], visible: false },
                  yAxis: { visible: false },
                  tooltip: { pointFormat: '<span>{series.name}</span>: {point.y} <b>({point.percentage:.0f}%)</b><br />' },
                  plotOptions: { bar: { stacking: 'percent' } } },
      size: { height: 200 },
      series: getSeriesData(5),
      title: { text: getSeriesName(5) },
    },
    {
      options: {  chart: { type: 'bar' },
                  xAxis: { categories: ["<b>Info</b>"], visible: false },
                  yAxis: { visible: false },
                  tooltip: { pointFormat: '<span>{series.name}</span>: {point.y} <b>({point.percentage:.0f}%)</b><br />' },
                  plotOptions: { bar: { stacking: 'percent' } } },
      size: { height: 200 },
      series: getSeriesData(6),
      title: { text: getSeriesName(6) },
    },
    {
      options: {  chart: { type: 'bar' },
                  xAxis: { categories: ["<b>Info</b>"], visible: false },
                  yAxis: { visible: false },
                  tooltip: { pointFormat: '<span>{series.name}</span>: {point.y} <b>({point.percentage:.0f}%)</b><br />' },
                  plotOptions: { bar: { stacking: 'percent' } } },
      size: { height: 200 },
      series: getSeriesData(7),
      title: { text: getSeriesName(7) },
    },
    {
      options: {  chart: { type: 'bar' },
                  xAxis: { categories: ["<b>Info</b>"], visible: false },
                  yAxis: { visible: false },
                  tooltip: { pointFormat: '<span>{series.name}</span>: {point.y} <b>({point.percentage:.0f}%)</b><br />' },
                  plotOptions: { bar: { stacking: 'percent' } } },
      size: { height: 200 },
      series: getSeriesData(8),
      title: { text: getSeriesName(8) },
    },
  ]


  /*
  GRANT INFO
    grant.id
    grant.professorName
    grant.province
    grant.organizationName
    grant.field
    grant.subject
    grant.year
    grant.amount
  NUMERICAL FIELDS
    # of Grants
    Grant Amount
    Year
  ENUMERABLE FIELDS
    Province
    Year
    Amount
  SEARCH INFO
    Field of Research
      x: Organization     y: Grants, Amount
      x: Province         y: Grants, Amount
      x: Year             y: Grants, Amount
      x: Subject          y: Grants, Amount
    Organization
      x: Year             y: Grants, Amount
      x: Professor        y: Grants, Amount
      x: Field            y: Grants, Amount
    Professor
      x: Field            y: Grants, Amount
      x: Subject          y: Grants, Amount
      x: Year             y: Grants, Amount
    Subject
      Belongs to field:   Field
      x: Organization     y: Grants, Amount
      x: Professor        y: Grants, Amount
      x: Province         y: Grants, Amount
    Province
      x: Amount           y: Grants             ** Line Graph
      x: Year             y: Grants, Amount
      x: Organization     y: Grants, Amount
      x: Professor        y: Amount
    Year
      x: Province         y: Grants, Amount
      x: Amount           y: Grants             ** Line Graph
    Amount
      x: Province         y: Grants, Amount
      x: Year             y: Grants, Amount
  */

  // array of booleans pertaining to whether the specific graph should be visible or not
  var showSeries = [false, false, false, false, false, false, false, false, false]

  // clears all graph data and charts
  function resetCharts() {
    showSeries = [false, false, false, false, false, false, false, false, false]
    seriesData = [[], [], [], [], [], [], [], [], []]
    seriesNames = ['', '', '', '', '', '', '', '', '']
    resetMapValues()
  }


  return {
    all: function() {
      return grants
    },

    showSeries: function(id) {
        return showSeries[id]
    },

    getChart: function(id) {
      return series[id]
    },

    getSomeGrants: function(num) {
      return grants.slice(0,num+1)
    },

    getNumberOfGrants: function() {
      return grants.length
    },

    set: function(grantResults, st, sf) {
      resetCharts()

      grants = grantResults
      searchType = st
      searchFor = sf

      if(st == 'f') {
        Alchemist.generateSeries(st, sf, 'o', 'a').success(function(data, status, headers, config) {setSeriesData(1, data); setSeriesName(1, 'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'o', 'z').success(function(data, status, headers, config) {setSeriesData(2, data); setSeriesName(2, 'Number of Grants (#)')})
        Alchemist.generateSeries(st, sf, 'p', 'a').success(function(data, status, headers, config) {setSeriesData(3, data); makeMap(getSeriesData(3)); setSeriesName(3, 'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'p', 'z').success(function(data, status, headers, config) {setSeriesData(4, data); setSeriesName(4, 'Number of Grants (#)')})
        Alchemist.generateSeries(st, sf, 'y', 'a').success(function(data, status, headers, config) {setSeriesData(5, data); setSeriesName(5, 'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'y', 'z').success(function(data, status, headers, config) {setSeriesData(6, data); setSeriesName(6, 'Number of Grants (#)')})
        Alchemist.generateSeries(st, sf, 's', 'a').success(function(data, status, headers, config) {setSeriesData(7, data); setSeriesName(7, 'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 's', 'z').success(function(data, status, headers, config) {setSeriesData(8, data); setSeriesName(8, 'Number of Grants (#)')})

        showSeries = [true, true, true, true, true, true, true, true, true]
      } else if(st == 'o') {
        Alchemist.generateSeries(st, sf, 'f', 'a').success(function(data, status, headers, config) {setSeriesData(1,data); setSeriesName(1,'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'f', 'z').success(function(data, status, headers, config) {setSeriesData(2,data); setSeriesName(2,'Number of Grants (#)')})
        Alchemist.generateSeries(st, sf, 'p', 'a').success(function(data, status, headers, config) {setSeriesData(3,data); makeMap(getSeriesData(3)); setSeriesName(3,'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'p', 'z').success(function(data, status, headers, config) {setSeriesData(4,data); setSeriesName(4,'Number of Grants (#)')})
        Alchemist.generateSeries(st, sf, 'y', 'a').success(function(data, status, headers, config) {setSeriesData(5,data); setSeriesName(5,'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'y', 'z').success(function(data, status, headers, config) {setSeriesData(6,data); setSeriesName(6,'Number of Grants (#)')})

        showSeries = [true, true, true, true, true, true, true, false, false]
      } else if(st == 'n') {
        Alchemist.generateSeries(st, sf, 'f', 'a').success(function(data, status, headers, config) {setSeriesData(1,data); setSeriesName(1,'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'f', 'z').success(function(data, status, headers, config) {setSeriesData(2,data); setSeriesName(2,'Number of Grants (#)')})
        Alchemist.generateSeries(st, sf, 's', 'a').success(function(data, status, headers, config) {setSeriesData(3,data); setSeriesName(3,'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 's', 'z').success(function(data, status, headers, config) {setSeriesData(4,data); setSeriesName(4,'Number of Grants (#)')})
        Alchemist.generateSeries(st, sf, 'y', 'a').success(function(data, status, headers, config) {setSeriesData(5,data); setSeriesName(5,'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'y', 'z').success(function(data, status, headers, config) {setSeriesData(6,data); setSeriesName(6,'Number of Grants (#)')})
        showSeries = [false, true, true, true, true, true, true, false, false]
      } else if(st == 's') {
        Alchemist.generateSeries(st, sf, 'o', 'a').success(function(data, status, headers, config) {setSeriesData(1,data); setSeriesName(1,'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'o', 'z').success(function(data, status, headers, config) {setSeriesData(2,data); setSeriesName(2,'Number of Grants (#)')})
        Alchemist.generateSeries(st, sf, 'n', 'a').success(function(data, status, headers, config) {setSeriesData(3,data); setSeriesName(3,'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'n', 'z').success(function(data, status, headers, config) {setSeriesData(4,data); setSeriesName(4,'Number of Grants (#)')})
        Alchemist.generateSeries(st, sf, 'p', 'a').success(function(data, status, headers, config) {setSeriesData(5,data); makeMap(getSeriesData(5)); setSeriesName(5,'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'p', 'z').success(function(data, status, headers, config) {setSeriesData(6,data); setSeriesName(6,'Number of Grants (#)')})
        showSeries = [true, true, true, true, true, true, true, false, false]
      } else if(st == 'p') {
        Alchemist.generateSeries(st, sf, 'y', 'a').success(function(data, status, headers, config) {setSeriesData(1,data); setSeriesName(1,'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'y', 'z').success(function(data, status, headers, config) {setSeriesData(2,data); setSeriesName(2,'Number of Grants (#)')})
        Alchemist.generateSeries(st, sf, 'o', 'a').success(function(data, status, headers, config) {setSeriesData(3,data); setSeriesName(3,'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'o', 'z').success(function(data, status, headers, config) {setSeriesData(4,data); setSeriesName(4,'Number of Grants (#)')})
        showSeries = [false, true, true, true, true, false, false, false, false]
      } else if(st == 'y') {
        Alchemist.generateSeries(st, sf, 'p', 'a').success(function(data, status, headers, config) {setSeriesData(1,data); makeMap(getSeriesData(1)); setSeriesName(1,'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'p', 'z').success(function(data, status, headers, config) {setSeriesData(2,data); setSeriesName(2,'Number of Grants (#)')})
        showSeries = [true, true, true, false, false, false, false, false, false]
      } else if(st == 'a') {
        Alchemist.generateSeries(st, sf, 'p', 'a').success(function(data, status, headers, config) {setSeriesData(1,data); makeMap(getSeriesData(1)); setSeriesName(1,'Grant Amount ($)')})
        Alchemist.generateSeries(st, sf, 'p', 'z').success(function(data, status, headers, config) {setSeriesData(2,data); setSeriesName(2,'Number of Grants (#)')})

        showSeries = [true, true, true, false, false, false, false, false, false]
      }
    },

    get: function(grantID) {
      for (var i = 0; i < grants.length; i++) {
        if (grants[i].id === parseInt(grantID)) {
          return grants[i]
        }
      }
      return null
    },

    // getSeries_GrantAmountByProvince: function() {
    //   return [{
    //       name: 'Ontario',
    //       data: [100]
    //   }, {
    //       name: 'Ottawa',
    //       data: [50]
    //   }, {
    //       name: 'Quebec',
    //       data: [130]
    //   }]
    // },
  }
})
