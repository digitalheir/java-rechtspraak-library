import _ from 'underscore';
import dataTfIdf from './raw-data.js';

function tfidfForAllTerms(topN) {
    var sorted = _.map(dataTfIdf, function (e) {
        return [e.term, e.score]
    });
    if (topN)
        sorted.splice(topN);
    return sorted;
}

export default tfidfForAllTerms;