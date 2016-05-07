const _ = require('underscore');

const dataTfIdf = require('./raw-data.js');


function tfidfForAllTerms(topN) {
    var sorted = _.map(dataTfIdf, function (e) {
        return [e.term, e.score]
    });
    if (topN) sorted.splice(topN);

    console.log(sorted)
    return sorted;
}

module.exports = {
    tfidfForAllTerms: tfidfForAllTerms
};