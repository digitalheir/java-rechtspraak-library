import _ from 'underscore';
import Immutable from 'immutable';

import json from './data.js';

var getMarkupStatsBarData = function (rows) {
  // NOTE: this is an old comment
  //
  // getMarkupStats guarantees that it has a bijection for the set of marked up and
  // not marked up, where the mapping function is the identity (i.e., for each year
  // in one set, that year exists in the other set), and also that they are in order.
  //
  //


  /**
   * an ordered array from objects to titles and years to counts
   * @type {{}}
   */
  var data = [];
  _.each(rows, function (el) {
    var index = el.key[0][0];
    var title = el.key[0][1];
    var year = el.key[1];
    var count = el.value;

    data[index] = data[index] || {
        title: title,
        data: {}
      };
    data[index].data[year] = (data[index].data[year] || 0) + count; //Add to this year's data
  });

  // make sure each title contains every x value
  var xVals = Immutable.Set();
  _.each(data, (bucket)=> {
    xVals = xVals.union(Immutable.Set(_.keys(bucket.data)));
  });
  _.each(data, (bucket)=> {
    xVals.forEach(function (xVal) {
      if (!bucket.data[xVal]) bucket.data[xVal] = 0;
    })
  });

  // Convert all hashes to {x, y} objects, sort
  var getObj = (title) => function (count, year) {
    return {"x": year, "y": count, "title": title};
  };
  var sort = function (o) {
    return o.x
  };
  _.each(data, (bucket)=> {
    bucket.data = _.sortBy(_.map(bucket.data, getObj(bucket.title)), sort);
  });

  //console.log(data);

  return data;
};


var markupStats = getMarkupStatsBarData(json.rows);

export default markupStats;
