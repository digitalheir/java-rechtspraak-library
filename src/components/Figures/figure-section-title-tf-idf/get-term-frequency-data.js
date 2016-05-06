const _ = require('underscore');

const rawData = {
    beslissing: (require('./section/beslissing')),
    overwegingen: (require('./section/overwegingen')),
    procesverloop: (require('./section/procesverloop'))
};

function tfidfForAllTerms(topN) {
    var returnObj = Object.create(null);
    for (var sectionRole in rawData) {
        if (rawData.hasOwnProperty(sectionRole)) {
            var sorted = _.sortBy(rawData[sectionRole], function (e) {
                return -e[1];
            });
            if (topN) {
                sorted.splice(topN);
            }
            returnObj[sectionRole] = sorted;
        }
    }
    return returnObj;
}


module.exports = {
    tfidfForAllTerms: tfidfForAllTerms
};