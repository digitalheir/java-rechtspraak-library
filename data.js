const cssnext = require('cssnext');
const chapters = require('./chapters');


const title = require('./title');

module.exports = {
    title: title,
    routes: ['/'].concat(chapters.inOrder.map(chapter=>chapter.route)).concat(['/full/'])
};