const request = require('sync-request');
function downloadString(url) {
    var res = request('GET', url);
    return (res.body.toString('utf-8'));
}

module.exports = downloadString;