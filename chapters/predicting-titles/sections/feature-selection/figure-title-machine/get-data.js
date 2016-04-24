var oboe = require('oboe');

var url =
    "https://rechtspraak.cloudant.com/docs/_design/stats/_view/section_title_pattern?group_level=1";

oboe(url)
    .done(function (res) {
        res.rows.forEach(function(row){

        });
    })
    .fail(function (e) {
        throw e;
    });