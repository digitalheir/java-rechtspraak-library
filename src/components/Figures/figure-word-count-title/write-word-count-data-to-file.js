const _ = require('underscore');
const downloadString = require('../../../../download-string-sync');
const fs = require ('fs');

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

fs.writeFile('./data-full.js', "export default " + JSON.stringify({
        url: baseUrl,
        data: values,
        totalCount: totalCount
    }));
fs.writeFile('./data.js', "export default "+JSON.stringify({
    url: baseUrl,
    data: values.splice(15),
    totalCount: totalCount
}));
