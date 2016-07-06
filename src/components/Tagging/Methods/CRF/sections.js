const crfSections = {
    undirectedGraphicalModels: {
        id: 'undirected-graphical-models',
        title: 'Undirected Graphical Models',
    },
    hmm: {
        id: 'hmm',
        title: 'Hidden Markov Models',
    },
    linearChain: {
        id: 'linear-chain-crf',
        title: 'Linear Chain Conditional Random Fields',
    },
    crfPerformance: {
        id: 'crf-performance',
        title: 'Performance',
    },
    inference: {
        id: 'crf-inference',
        title: 'Inference',
    },
    parameterEstimation: {
        id: 'crf-parameter-estimation',
        title: 'Parameter Estimation',
    }
};

crfSections.inOrder = [
    crfSections.hmm,
    crfSections.undirectedGraphicalModels,
    crfSections.linearChain,
    crfSections.parameterEstimation,
    crfSections.inference,
    // crfSections.crfPerformance,
];


export default crfSections;
