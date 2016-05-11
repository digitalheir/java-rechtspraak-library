const downloadString = require('../../../../download-string-sync');
const fs = require('fs');
var encodeUri = require("querystring").escape;

var OTHER = '_OTHER';

var URL = "https://rechtspraak.cloudant.com/docs/" +
    "_design/stats/" +
    "_list/value_large_enough/" +
    "section_roles?" +
    "group_level=2" +
    "&stale=ok" ;

function download() {
    var result = JSON.parse(downloadString(URL));

// Transform results
    var aggr = {};
    result.forEach(function (row) {
        aggr[row.key[0]] = aggr[row.key[0]] ? aggr[row.key[0]] : {};
//                    if (row.value < 10) {
//                        aggr[row.key[0]][OTHER] = aggr[row.key[0]][OTHER] ?
//                        aggr[row.key[0]][OTHER] + row.value : row.value;
//                    } else {
        var pattern = row.key[1].trim().length <= 0 ? '_EMPTY' : row.key[1];
        aggr[row.key[0]][pattern] = row.value;
//                    }
    });

    var rootChildren = [];
    for (var name in aggr) {
        if (aggr.hasOwnProperty(name)) {
            var patternChildren = [];
            var aggrPatterns = aggr[name];
            for (var pattern in aggrPatterns) {
                if (aggrPatterns.hasOwnProperty(pattern)) {
                    patternChildren.push({
                        name: pattern,
                        size: aggrPatterns[pattern]
                    });
                }
            }

            rootChildren.push({
                name: name,
                children: patternChildren
            });
        }
    }
    return {
        name: "Title patterns",
        children: rootChildren
    };
}
fs.writeFile('./raw-data.js',
    "export default " + JSON.stringify({
        data: download(),
        href: URL
    }));
