const _ = require('underscore');
const downloadString = require('../../../../../download-string-sync');

var baseUrl = "https://rechtspraak.cloudant.com/docs/_design/stats/_view/" +
    "word_count_for_title_elements?group_level=1&stale=ok";

var json = JSON.parse(downloadString(baseUrl));

var totalCount = _.reduce(json.rows, function (count,row) {
    return count + row.value;
}, 0);

var values = _.sortBy(_.map(json.rows, function (row) {
    return {wordCount: row.key, titleCount: (row.value)}
}), function (el) {
    return el.wordCount;
});


module.exports = {
    url: baseUrl,
    data: values,
    totalCount: totalCount
};