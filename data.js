var cssnext = require('cssnext');
var chapters = require('./chapters');


module.exports = {
    title: 'Enriching Dutch Case Law Markup',
    routes: ['/'].concat(chapters.inOrder.map(chapter=>chapter.route))
};