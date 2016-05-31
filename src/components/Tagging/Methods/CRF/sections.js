const crfSections = {
    graphicalModels: {
        id: 'graphical-models',
        title: 'Graphical Models',
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
    crfSections.graphicalModels,
    crfSections.hmm,
    crfSections.linearChain,
    crfSections.parameterEstimation,
    crfSections.inference,
    // crfSections.crfPerformance,
];


export default crfSections;
