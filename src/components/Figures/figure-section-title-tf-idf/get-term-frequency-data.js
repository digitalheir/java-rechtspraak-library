import _ from 'underscore';
import beslissing from './section/beslissing';
import overwegingen from './section/overwegingen';
import procesverloop from './section/procesverloop';

const rawData = {
    beslissing: beslissing,
    overwegingen: overwegingen,
    procesverloop: procesverloop
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


export default tfidfForAllTerms;