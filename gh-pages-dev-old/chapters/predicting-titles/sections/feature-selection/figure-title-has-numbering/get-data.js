const downloadString = require('../../../../../download-string-sync');

var URL =
    "https://rechtspraak.cloudant.com/docs/_design/stats/_view/section_title_has_numbering?group_level=2";
const doesntHaveNr = "Does not have numbering";
const hasNr = "Has numbering";

function download() {
    var result = JSON.parse(downloadString(URL));

    var noNumbering = 0;
    var countOther = 0;
    var numberings = [];
    result.rows.forEach(function (row) {
        if (row.key[0]) {
            if (row.value <= 5) {
                countOther += row.value;
            } else {
                numberings.push({
                    name: row.key[1].join(' '),
                    value: row.value
                });
            }
        } else {
            noNumbering = row.value;
        }
    });

    numberings.push({
        name: '[Other]',
        value: countOther
    });

    return {
        name: "root",
        children: [
            {
                name: doesntHaveNr,
                size: noNumbering
            },
            {
                name: hasNr,
                children: numberings
            }]
    };
}

module.exports = {
    doesntHaveNr: doesntHaveNr,
    hasNr: hasNr,
    download: download,
    href: URL
};
