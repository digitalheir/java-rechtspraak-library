const _ = require('underscore');

const dataTfIdf = require('./raw-data.js');
const fs = require('fs');


function tfidfForAllTerms(topN) {
    var sorted = _.map(dataTfIdf, function (e) {
        return [e.term, e.score]
    });
    if (topN)
        sorted.splice(topN);
    return sorted;
}

fs.writeFile('./data.js',"export default "+JSON.stringify(tfidfForAllTerms(100)));