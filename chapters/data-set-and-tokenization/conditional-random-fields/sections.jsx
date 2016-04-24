const React = require('react');
const _ = require('underscore');

const createSection = function (id, title) {
    return {
        id: id,
        title: title
    }
};

const linearChain = createSection(
    'linear-chain',
    "Linear-Chain Conditional Random Fields"
);
const intro = createSection(
    'introduction',
    "Introduction"
);
const graphicalModels = createSection(
    'graphical-models',
    "Graphical Models"
);
const hmm = createSection(
    'hidden-markov-models',
    "Hidden Markov Models"
);
const logres = createSection(
    'logistic-regression',
    "Logistic Regression"
);
const inPractice = createSection(
    'in-practice',
    "CRFs in Practice"
);

const sections = {
    intro: intro,
    graphicalModels: graphicalModels,
    hmm: hmm,
    logres: logres,
    linearChain: linearChain,
    inPractice: inPractice
};

sections.inOrder = [intro, graphicalModels, hmm, logres, linearChain,inPractice];

//         id: "",
//         component: 
//     },
//     hmm:{
//         title: "",
//         id: "",
//         component: require('./sections/hmm')
//     },
//     logres:{
//         title: "",
//         id: "",
//         component: require('./sections/')
//     }
// };

module.exports = sections;


// }, {
//     "title": "MapReduce CRF",
//     "id": "mapreduce-crf"
// }]