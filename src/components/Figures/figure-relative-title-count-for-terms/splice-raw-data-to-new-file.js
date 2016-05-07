const raw = require('./raw-data');
const fs= require('fs');

const numberOfTerms = 10;


fs.writeFile('./data.js',"export default "+JSON.stringify( {
    numberOfTerms: numberOfTerms, data: raw.splice(numberOfTerms)
}));